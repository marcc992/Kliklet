package es.marcmauri.kliklet.features.storesviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.databinding.FragmentStoresViewerListBinding
import es.marcmauri.kliklet.features.storesviewer.StoresViewerListMVP
import es.marcmauri.kliklet.features.storesviewer.view.adapter.StoresViewerListAdapter
import es.marcmauri.kliklet.features.storesviewer.view.fragment.placeholder.PlaceholderContent
import es.marcmauri.kliklet.features.storesviewer.view.listener.RecyclerStoresViewerListListener
import es.marcmauri.kliklet.utils.snackBar
import javax.inject.Inject


class StoresViewerListFragment : Fragment(), StoresViewerListMVP.View {

    @Inject
    lateinit var presenter: StoresViewerListMVP.Presenter
    private lateinit var binding: FragmentStoresViewerListBinding
    private lateinit var adapter: StoresViewerListAdapter
    private var storeList: ArrayList<String> = ArrayList(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as KlikletApp).getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStoresViewerListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.onFragmentReady()
    }

    override fun configureUI() {
        setAdapter()
        setRecyclerView()
    }

    private fun setAdapter() {
        adapter = StoresViewerListAdapter(PlaceholderContent.ITEMS, object : RecyclerStoresViewerListListener {
            override fun onPhotoItemClick(todo: String, position: Int) {
                // todo: position sirve para algo?
                presenter.onStoreItemClick(todo)
            }
        })
    }

    private fun setRecyclerView() {
        binding.recyclerViewStoreList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewStoreList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewStoreList.adapter = adapter
    }

    override fun showStoreList(todo: List<String>) {
        showError("Store list ready to show! Items: ${todo.size}")
    }

    override fun goToStoreDetails(todo: String) {
        showError("It is time to go to $todo store detail!")
    }

    override fun showLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }

    override fun showError(message: String) {
        snackBar(
            message = message,
            view = binding.rootView
        )
    }


}