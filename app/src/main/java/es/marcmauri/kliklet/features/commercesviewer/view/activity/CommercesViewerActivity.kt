package es.marcmauri.kliklet.features.commercesviewer.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.databinding.ActivityCommercesViewerBinding
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerListFragment
import es.marcmauri.kliklet.common.snackBar

class CommercesViewerActivity : FragmentActivity() {

    private lateinit var binding: ActivityCommercesViewerBinding
    private val commercesViewerListFragment by lazy { CommercesViewerListFragment() }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CODE_LOCATION = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommercesViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as KlikletApp).getComponent().inject(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check if the device has the Google Services installed & available
        checkLocationPermissionsAndContinue()

        // Set a simple listener to the back button from the Toolbar
        binding.ivToolbarBack.setOnClickListener { this.onBackPressed() }

        // Load the Commerces List Fragment
        loadFragment(commercesViewerListFragment)
    }

    fun loadFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            val transaction = supportFragmentManager.beginTransaction()

            // When we try to load a different fragment than the list, it means we are opening
            // a commerce detail fragment. In this case we hide the list fragment to not lose its
            // contents and view. Besides, we leverage that situation to restore the toolbar
            if (fragment !is CommercesViewerListFragment) {
                transaction
                    .addToBackStack(null)
                    .hide(commercesViewerListFragment)
            }

            transaction
                .add(binding.fragmentContainer.id, fragment)
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    fun putCommerceNameToToolbar(title: String) {
        binding.ivToolbarBack.visibility = View.VISIBLE
        binding.tvToolbarTitle.text = title
        binding.tvToolbarTitle.setTypeface(null, Typeface.NORMAL)
    }

    private fun removeCommerceNameFromToolbar() {
        binding.ivToolbarBack.visibility = View.GONE
        binding.tvToolbarTitle.text = getString(R.string.activity_commerces_viewer_toolbar_title)
        binding.tvToolbarTitle.setTypeface(null, Typeface.BOLD)
    }

    private fun checkLocationPermissionsAndContinue() {
        if (checkGoogleServicesAvailable()) {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->

                val locationPermissionsGranted = when {
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> true
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> true
                    else -> false
                }

                if (locationPermissionsGranted) {
                    getLastKnownLocation()
                }
            }

            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            snackBar("Your devices has not support to Google Services", binding.root)
        }
    }


    private fun checkGoogleServicesAvailable() = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS


    /**
     * [private ]This method gets the last known location from the user. It is called just after
     * checking the user location permissions, either FINE or COARSE
     */
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    prefs.lastLocation = LatLng(location.latitude, location.longitude)
                }
            }
    }


    /**
     * This method will be called each time a detail fragment was destroyed, so we can restore
     * the toolbar here.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        removeCommerceNameFromToolbar()
    }
}