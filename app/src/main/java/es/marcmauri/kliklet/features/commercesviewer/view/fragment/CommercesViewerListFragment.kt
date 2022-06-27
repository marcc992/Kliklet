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
import es.marcmauri.kliklet.databinding.FragmentCommercesViewerListBinding
import es.marcmauri.kliklet.features.commercesviewer.model.entities.CategoryInfo
import javax.inject.Inject


class CommercesViewerListFragment : Fragment(), CommercesViewerListMVP.View {

    @Inject
    lateinit var presenter: CommercesViewerListMVP.Presenter
    private lateinit var binding: FragmentCommercesViewerListBinding

    private lateinit var buttonsAdapter: ButtonsViewerListAdapter
    private lateinit var categoriesAdapter: CategoriesViewerListAdapter
    private lateinit var commercesAdapter: CommercesViewerListAdapter
    private var buttonList: ArrayList<ButtonInfo> = ArrayList(0)
    private var categoryList: ArrayList<CategoryInfo> = ArrayList(0)
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
                    override fun onCategoryItemClick(category: CategoryInfo, position: Int) {
                        presenter.onCategoryItemClick(category, position)
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
            //LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
    private fun setButtonList(newButtonsList: List<ButtonInfo>) {
        // 1. Remove all previous buttons if so
        val lastButtonsCount = buttonsAdapter.itemCount
        buttonList.clear()
        buttonsAdapter.notifyItemRangeRemoved(0, lastButtonsCount)
        // 2. Add all new buttons
        buttonList.addAll(newButtonsList)
        // 3. Notify the adapter with the new data
        buttonsAdapter.notifyItemRangeInserted(0, newButtonsList.size)
    }

    private fun updateButtonList(modifiedButtonsList: List<ButtonInfo>) {
        modifiedButtonsList.forEachIndexed { position, buttonInfo ->
            if (buttonList[position] != (buttonInfo)) {
                // The button is updated only and only the current one is different
                buttonList[position]= buttonInfo
                buttonsAdapter.notifyItemChanged(position)
            }
        }
    }

    override fun showButtonList(newButtonsList: List<ButtonInfo>) {
        if (buttonList.size != newButtonsList.size) {
            // When the sizes of current button list and the new button list mismatch, then we need
            // to set the entire button list
            setButtonList(newButtonsList)
        } else {
            // When the sizes are the sem, we just need to update the current buttons
            updateButtonList(newButtonsList)
        }
    }

//    private fun setCategoryList(newCategoryList: List<CategoryInfo>) {
//        // 1. Remove all previous categories if so
//        val lastCategoryItemCount = categoriesAdapter.itemCount
//        categoryList.clear()
//        categoriesAdapter.notifyItemRangeRemoved(0, -lastCategoryItemCount)
//        // 2. Add all new categories
//        categoryList.addAll(newCategoryList)
//        // 3. Notify the adapter with the new data
//        categoriesAdapter.notifyItemRangeInserted(0, newCategoryList.size)
//    }

    private fun setCategoryList(newCategoryList: List<CategoryInfo>) {
        // 1. Remove all previous categories if so
        val lastCategoryItemCount = categoriesAdapter.itemCount
        categoryList.clear()
        categoriesAdapter.notifyItemRangeRemoved(0, lastCategoryItemCount)
        // 2. Add all new categories
        categoryList.addAll(newCategoryList)
        // 3. Notify the adapter with the new data
        categoriesAdapter.notifyItemRangeInserted(0, newCategoryList.size)


        // 1. Remove all previous categories if so
        val lastCategoriesCount = categoriesAdapter.itemCount
        categoryList.clear()
        categoriesAdapter.notifyItemRangeRemoved(0, lastCategoriesCount)
        // 2. Add all new categories
        categoryList.addAll(newCategoryList)
        // 3. Notify the adapter with the new data
        categoriesAdapter.notifyItemRangeInserted(0, newCategoryList.size)
    }

    private fun updateCategoryList(modifiedCategoryList: List<CategoryInfo>) {
        modifiedCategoryList.forEachIndexed { position, categoryInfo ->
            if (categoryList[position] != (categoryInfo)) {
                // The category is updated only and only the current one is different
                categoryList[position]= categoryInfo
                categoriesAdapter.notifyItemChanged(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showCategoryList(newCategoryList: List<CategoryInfo>) {

        if (categoryList.size != newCategoryList.size) {
            // When the sizes of current category list and the new category list mismatch, then we
            // need to set the entire category list
            setCategoryList(newCategoryList)
        } else {
            // When the sizes are the sem, we just need to update the current buttons
            updateCategoryList(newCategoryList)
        }




//        if (categoryList.isNotEmpty()) {
//            binding.recyclerViewCategoryList.smoothScrollToPosition(0)
//            categoryList.clear()
//            categoriesAdapter.notifyDataSetChanged()
//        }
//
//        binding.recyclerViewCategoryList.post {
//
//            // Insert Categories one by one to get fancy view effects
//            newCategoryList.forEach { category ->
//                categoryList.add(category)
//                binding.recyclerViewCategoryList.post {
//                    categoriesAdapter.notifyItemInserted(categoryList.size - 1)
//                }
//            }
//        }
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