package es.marcmauri.kliklet.features.storesviewer.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.features.storesviewer.view.adapter.StoreListAdapter
import es.marcmauri.kliklet.features.storesviewer.view.fragment.placeholder.PlaceholderContent


class StoreListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as KlikletApp).getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_store_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = StoreListAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }


}