package es.marcmauri.kliklet.features.commercesviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.databinding.FragmentCommercesViewerListBinding
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CommercesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCommercesViewerListListener
import es.marcmauri.kliklet.utils.snackBar
import javax.inject.Inject


class CommercesViewerListFragment : Fragment(), CommercesViewerListMVP.View {

    @Inject
    lateinit var presenter: CommercesViewerListMVP.Presenter
    private lateinit var binding: FragmentCommercesViewerListBinding
    private lateinit var adapter: CommercesViewerListAdapter
    private var commerceList: ArrayList<Commerce> = ArrayList(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as KlikletApp).getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommercesViewerListBinding.inflate(layoutInflater)
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
        adapter = CommercesViewerListAdapter(commerceList, object : RecyclerCommercesViewerListListener {
            override fun onPhotoItemClick(commerce: Commerce, position: Int) {
                // todo: position sirve para algo?
                presenter.onCommerceItemClick(commerce)
            }
        })
    }

    private fun setRecyclerView() {
        binding.recyclerViewCommerceList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewCommerceList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCommerceList.adapter = adapter
    }

    override fun showCommerceList(newCommerceList: List<Commerce>) {
        showError("Commerce list ready to show! Items: ${newCommerceList.size}")
        newCommerceList.forEach { commerce ->
            commerceList.add(commerce)
            binding.recyclerViewCommerceList.post {
                adapter.notifyItemInserted(commerceList.size - 1)
            }
        }
    }

    override fun goToCommerceDetails(commerce: Commerce) {
        showError("It is time to go to ${commerce.name} (Cat: ${commerce.category}) commerce detail!")
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