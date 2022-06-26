package es.marcmauri.kliklet.features.commercesviewer.view.fragment

import android.annotation.SuppressLint
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
import es.marcmauri.kliklet.features.commercesviewer.view.activity.CommercesViewerActivity
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.ButtonsViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CategoriesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CommercesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerButtonsViewerListListener
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCategoriesViewerListListener
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCommercesViewerListListener
import es.marcmauri.kliklet.common.snackBar
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
                    presenter.onButtonItemClick(buttonInfo, position)
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
                override fun onCommerceItemClick(commerce: Commerce, position: Int) {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun showButtonList(newButtonsList: List<ButtonInfo>) {
        // 1. Remove all previous buttons if so
        buttonList.clear()
        // 2. Add all new buttons
        buttonList.addAll(newButtonsList)
        // 3. Notify the adapter with all data at once to avoid fancy view effects
        buttonsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showCategoryList(newCategoriesList: List<String>) {
        if (categoryList.isNotEmpty()) {
            binding.recyclerViewCategoryList.smoothScrollToPosition(0)
            categoryList.clear()
            categoriesAdapter.notifyDataSetChanged()
        }

        binding.recyclerViewCategoryList.post {

            // Insert Categories one by one to get fancy view effects
            newCategoriesList.forEach { category ->
                categoryList.add(category)
                binding.recyclerViewCategoryList.post {
                    categoriesAdapter.notifyItemInserted(categoryList.size - 1)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showCommerceList(newCommerceList: List<Commerce>) {
        // It is needed to clear all previous values when we show a new commerces list
        if (commerceList.isNotEmpty()) {
            binding.recyclerViewCommerceList.smoothScrollToPosition(0)
            commerceList.clear()
            commercesAdapter.notifyDataSetChanged()
        }

        binding.recyclerViewCommerceList.post {

            // Insert commerces one by one to get fancy view effects
            newCommerceList.forEach { commerce ->
                commerceList.add(commerce)
                binding.recyclerViewCommerceList.post {
                    commercesAdapter.notifyItemInserted(commerceList.size - 1)
                }
            }
        }
    }

    override fun goToCommerceDetails(commerce: Commerce) {
        (activity as CommercesViewerActivity).loadFragment(
            CommercesViewerDetailFragment.newInstance(
                commerce
            )
        )
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