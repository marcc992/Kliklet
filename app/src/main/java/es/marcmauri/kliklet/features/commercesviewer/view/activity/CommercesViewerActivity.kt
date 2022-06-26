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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.common.checkGoogleServicesAvailable
import es.marcmauri.kliklet.common.snackBar
import es.marcmauri.kliklet.databinding.ActivityCommercesViewerBinding
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerDetailFragment
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerListFragment

class CommercesViewerActivity : FragmentActivity() {

    private lateinit var binding: ActivityCommercesViewerBinding
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val commercesViewerListFragment by lazy { CommercesViewerListFragment() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommercesViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dagger injection
        (application as KlikletApp).getComponent().inject(this)

        // Set a simple listener to the back button from the Toolbar
        binding.ivToolbarBack.setOnClickListener { this.onBackPressed() }

        // Check if the device has the Google Services installed & available, granted or not,
        // and proceed with the load of the Commerce list fragment
        checkLocationPermissionsAndContinue()
    }

    fun loadFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            val transaction = supportFragmentManager.beginTransaction()

            // When we are going to load the Commerce detail fragment, we do not want to destroy
            // the current one about Commerce list fragment to not lose the current view.
            // Besides, we want the new one's behavior will be like an activity,
            // allowing the onBackPressed method to return to he list.
            if (fragment is CommercesViewerDetailFragment) {
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

    // Todo: put this into ExtensionFunctions, passing an event as lamda
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
                } else {
                    snackBar(
                        message = "You didn't allow the location, so you will not be able to " +
                                "enjoy all the features",
                        view = binding.root,
                        duration = Snackbar.LENGTH_LONG
                    )
                }

                // When we have the user consent or not, we proceed launching the commerce list fragment
                loadFragment(commercesViewerListFragment)
            }

            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            // We cant access to the user location because their mobile does not have GServices.
            // Anyway, the fragment is loaded but with limited UX
            snackBar(
                message = "Google services are not allowed on your device, so you will not be " +
                        "able to enjoy all the features.",
                view = binding.root,
                duration = Snackbar.LENGTH_LONG
            )
            loadFragment(commercesViewerListFragment)
        }
    }

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

    /**
     * This method will be called each time a detail fragment was destroyed, so we can restore
     * the toolbar here.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        removeCommerceNameFromToolbar()
    }
}