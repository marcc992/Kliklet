package es.marcmauri.kliklet.features.storesviewer.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.databinding.ActivityStoresViewerBinding
import es.marcmauri.kliklet.features.storesviewer.view.fragment.StoreListFragment

class StoresViewerActivity : FragmentActivity() {

    private lateinit var binding: ActivityStoresViewerBinding
    private val storeListFragment by lazy { StoreListFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoresViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as KlikletApp).getComponent().inject(this)

        loadFragment(storeListFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            val transaction = supportFragmentManager.beginTransaction()

            // When we try to load a different fragment than the list, it means we are opening
            // a store detail fragment. In this case we hide the list fragment to not lose its
            // contents and view
            if (fragment !is StoreListFragment) {
                transaction
                    .addToBackStack(null)
                    .hide(storeListFragment)
            }

            transaction
                .add(binding.fragmentContainer.id, fragment)
                .setTransition(TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }
}