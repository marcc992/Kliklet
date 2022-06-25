package es.marcmauri.kliklet.features.commercesviewer.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
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

        loadFragment(commercesViewerListFragment)
    }

    fun loadFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            val transaction = supportFragmentManager.beginTransaction()

            // When we try to load a different fragment than the list, it means we are opening
            // a commerce detail fragment. In this case we hide the list fragment to not lose its
            // contents and view
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
}