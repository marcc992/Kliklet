package es.marcmauri.kliklet.features.commercesviewer.view.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.google.android.material.snackbar.Snackbar
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.common.checkLocationPermissions
import es.marcmauri.kliklet.common.getLastKnownLocation
import es.marcmauri.kliklet.common.snackBar
import es.marcmauri.kliklet.databinding.ActivityCommercesViewerBinding
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerDetailFragment
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerListFragment

class CommercesViewerActivity : FragmentActivity() {

    private lateinit var binding: ActivityCommercesViewerBinding
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
        // and proceed with the load of the Commerce list fragment (the first one)
        checkLocationPermissions(
            onGrantedEvt = {
                getLastKnownLocation()
                loadFragment(commercesViewerListFragment)
            },
            onRefusedEvt = {
                snackBar(
                    message = getString(R.string.error_message_location_not_allowed),
                    view = binding.root,
                    duration = Snackbar.LENGTH_LONG
                )
                loadFragment(commercesViewerListFragment)
            },
            onGServicesNotAvailable = {
                snackBar(
                    message = getString(R.string.error_message_google_services_not_available),
                    view = binding.root,
                    duration = Snackbar.LENGTH_LONG
                )
                loadFragment(commercesViewerListFragment)
            }
        )
    }

    /**
     * Fragment loader to this activity.
     *
     * When we are going to load the Commerce detail fragment, we do not want to destroy the current
     * one which is the Commerce list fragment because we want to keep the current view.
     * Besides, we want the new one's behavior like an activity, putting it to the back stack
     * allowing the  onBackPressed event listener.
     *
     * @param fragment: Fragment to load
     */
    fun loadFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            val transaction = supportFragmentManager.beginTransaction()

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

    /**
     * Toolbar title setter.
     *
     * @param title: The title to show into the Toolbar
     */
    fun putCommerceNameToToolbar(title: String) {
        binding.ivToolbarBack.visibility = View.VISIBLE
        binding.tvToolbarTitle.text = title
        binding.tvToolbarTitle.setTypeface(null, Typeface.NORMAL)
    }

    /**
     * Toolbar title remover. When it is called, then the current title is replaced for the
     * default one.
     */
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