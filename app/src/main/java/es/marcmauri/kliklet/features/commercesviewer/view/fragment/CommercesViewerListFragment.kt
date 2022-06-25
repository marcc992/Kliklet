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
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.ButtonsViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CategoriesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CommercesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerButtonsViewerListListener
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCategoriesViewerListListener
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCommercesViewerListListener
import es.marcmauri.kliklet.utils.snackBar
import javax.inject.Inject


class CommercesViewerListFragment : Fragment(), CommercesViewerListMVP.View {

    @Inject
    lateinit var presenter: CommercesViewerListMVP.Presenter
    private lateinit var binding: FragmentCommercesViewerListBinding

    private lateinit var buttonsAdapter: ButtonsViewerListAdapter
    private lateinit var categoriesAdapter: CategoriesViewerListAdapter
    private lateinit var commercesAdapter: CommercesViewerListAdapter
    private var buttonList: ArrayList<ButtonInfo> = ArrayList(0)
    private var categoryList: ArrayList<String> = ArrayList(0)
    private var commerceList: ArrayList<Commerce> = ArrayList(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as KlikletApp).getComponent().inject(this)
        presenter.setContext(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        setButtonsAdapter()
        setCategoriesAdapter()
        setCommerceAdapter()
        setRecyclerViews()
    }

    private fun setButtonsAdapter() {
        buttonsAdapter =
            ButtonsViewerListAdapter(buttonList, object : RecyclerButtonsViewerListListener {
                override fun onButtonItemClick(buttonInfo: ButtonInfo, position: Int) {
                    presenter.onButtonItemClick(buttonInfo)
                }
            })
    }

    private fun setCategoriesAdapter() {
        categoriesAdapter =
            CategoriesViewerListAdapter(
                categoryList,
                object : RecyclerCategoriesViewerListListener {
                    override fun onCategoryItemClick(category: String, position: Int) {
                        presenter.onCategoryItemClick(category)
                    }
                })
    }

    private fun setCommerceAdapter() {
        commercesAdapter =
            CommercesViewerListAdapter(commerceList, object : RecyclerCommercesViewerListListener {
                override fun onPhotoItemClick(commerce: Commerce, position: Int) {
                    // todo: position sirve para algo?
                    presenter.onCommerceItemClick(commerce)
                }
            })
    }

    private fun setRecyclerViews() {
        // Buttons recycler view
        binding.recyclerViewButtonList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewButtonList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewButtonList.adapter = buttonsAdapter

        // Categories recycler view
        binding.recyclerViewCategoryList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewCategoryList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCategoryList.adapter = categoriesAdapter

        // Commerces recycler view
        binding.recyclerViewCommerceList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewCommerceList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCommerceList.adapter = commercesAdapter
    }

    override fun showButtonList(newButtonsList: List<ButtonInfo>) {
        newButtonsList.forEach { buttonInfo ->
            buttonList.add(buttonInfo)
            binding.recyclerViewButtonList.post {
                buttonsAdapter.notifyItemInserted(buttonList.size - 1)
            }
        }
    }

    override fun showCategoryList(newCategoriesList: List<String>) {
        newCategoriesList.forEach { category ->
            categoryList.add(category)
            binding.recyclerViewCategoryList.post {
                categoriesAdapter.notifyItemInserted(categoryList.size - 1)
            }
        }
    }

    override fun showCommerceList(newCommerceList: List<Commerce>) {
        newCommerceList.forEach { commerce ->
            commerceList.add(commerce)
            binding.recyclerViewCommerceList.post {
                commercesAdapter.notifyItemInserted(commerceList.size - 1)
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