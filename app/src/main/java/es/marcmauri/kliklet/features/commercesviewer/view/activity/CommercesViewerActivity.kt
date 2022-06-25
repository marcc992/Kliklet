package es.marcmauri.kliklet.features.commercesviewer.view.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.databinding.ActivityCommercesViewerBinding
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerListFragment

class CommercesViewerActivity : FragmentActivity() {

    private lateinit var binding: ActivityCommercesViewerBinding
    private val commercesViewerListFragment by lazy { CommercesViewerListFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommercesViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as KlikletApp).getComponent().inject(this)

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

    /**
     * This method will be called each time a detail fragment was destroyed, so we can restore
     * the toolbar here.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        removeCommerceNameFromToolbar()
    }
}