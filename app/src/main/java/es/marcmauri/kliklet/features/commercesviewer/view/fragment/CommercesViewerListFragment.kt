package es.marcmauri.kliklet.features.commercesviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.common.snackBar
import es.marcmauri.kliklet.databinding.FragmentCommercesViewerListBinding
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.CategoryInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.view.activity.CommercesViewerActivity
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.ButtonsViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CategoriesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.adapter.CommercesViewerListAdapter
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerButtonsViewerListListener
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCategoriesViewerListListener
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCommercesViewerListListener
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
    }

    private fun setButtonsAdapter() {
        buttonsAdapter =
            ButtonsViewerListAdapter(buttonList, object : RecyclerButtonsViewerListListener {
                override fun onButtonItemClick(buttonInfo: ButtonInfo, position: Int) {
                    presenter.onButtonItemClick(buttonInfo, position)
                }
            })

        binding.recyclerViewButtonList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewButtonList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewButtonList.adapter = buttonsAdapter
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

        binding.recyclerViewCategoryList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewCategoryList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCategoryList.adapter = categoriesAdapter
    }

    private fun setCommerceAdapter() {
        commercesAdapter =
            CommercesViewerListAdapter(commerceList, object : RecyclerCommercesViewerListListener {
                override fun onCommerceItemClick(commerce: Commerce) {
                    presenter.onCommerceItemClick(commerce)
                }
            })

        binding.recyclerViewCommerceList.itemAnimator = DefaultItemAnimator()
        binding.recyclerViewCommerceList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCommerceList.adapter = commercesAdapter
    }

    private fun setButtonList(newButtonsList: List<ButtonInfo>) {
        // 1. Remove all previous buttons if so
        val lastButtonsCount = buttonsAdapter.itemCount
        buttonList.clear()
        buttonsAdapter.notifyItemRangeRemoved(0, lastButtonsCount)
        // 2. Add all new buttons
        buttonList.addAll(newButtonsList)
        buttonsAdapter.notifyItemRangeInserted(0, newButtonsList.size)
    }

    private fun updateButtonList(modifiedButtonsList: List<ButtonInfo>) {
        modifiedButtonsList.forEachIndexed { position, buttonInfo ->
            if (buttonList[position] != (buttonInfo)) {
                // The button is updated only when the new one is different
                buttonList[position] = buttonInfo
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
            // When the sizes are the same, we just need to update some of the current ones
            updateButtonList(newButtonsList)
        }
    }

    private fun setCategoryList(newCategoryList: List<CategoryInfo>) {
        // 1. Remove all previous categories if so
        val lastCategoryCount = categoriesAdapter.itemCount
        categoryList.clear()
        categoriesAdapter.notifyItemRangeRemoved(0, lastCategoryCount)
        // 2. Add all new categories
        categoryList.addAll(newCategoryList)
        categoriesAdapter.notifyItemRangeInserted(0, newCategoryList.size)
    }

    private fun updateCategoryList(modifiedCategoryList: List<CategoryInfo>) {
        modifiedCategoryList.forEachIndexed { position, categoryInfo ->
            if (categoryList[position] != (categoryInfo)) {
                // The category is updated only when the new one is different
                categoryList[position] = categoryInfo
                categoriesAdapter.notifyItemChanged(position)
            }
        }
    }

    override fun showCategoryList(newCategoryList: List<CategoryInfo>) {
        if (categoryList.size != newCategoryList.size) {
            // When the sizes of current category list and the new category list mismatch, then we
            // need to set the entire category list
            setCategoryList(newCategoryList)
        } else {
            // When the sizes are the same, we just need to update some of the current ones
            updateCategoryList(newCategoryList)
        }
    }

    override fun showCommerceList(newCommerceList: List<Commerce>) {
        // It is needed to clear all previous values when we show a new commerces list
        if (commerceList.isNotEmpty()) {
            // 1. Smooth scroll to the top of the list
            binding.recyclerViewCommerceList.smoothScrollToPosition(0)
            // 2. Remove all previous Commerces
            val lastCommerceCount = commercesAdapter.itemCount
            commerceList.clear()
            commercesAdapter.notifyItemRangeRemoved(0, lastCommerceCount)
        }

        commerceList.addAll(newCommerceList)
        commercesAdapter.notifyItemRangeInserted(0, commerceList.size)
    }

    override fun changeSelectedButton(selectedBtnPosition: Int) {
        buttonList.forEachIndexed { index, button ->
            button.selected = index == selectedBtnPosition
        }
        buttonsAdapter.notifyItemRangeChanged(0, buttonList.size)
    }

    override fun changeButtonCount(btnPosition: Int, count: Int) {
        buttonList[btnPosition].count = count
        buttonsAdapter.notifyItemChanged(btnPosition)
    }

    override fun changeSelectedCategory(selectedCategoryPosition: Int) {
        categoryList.forEachIndexed { index, category ->
            if (category.selected && index != selectedCategoryPosition) {
                category.selected = false
                categoriesAdapter.notifyItemChanged(index)
            } else if (index == selectedCategoryPosition) {
                category.selected = true
                categoriesAdapter.notifyItemChanged(index)
            } // else Do nothing because the current category does not have to change
        }
    }

    override fun goToCommerceDetails(commerce: Commerce) {
        (activity as CommercesViewerActivity).loadFragment(
            CommercesViewerDetailFragment.newInstance(commerce)
        )
    }

    override fun showLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }

    override fun showError(resId: Int) {
        showError(getString(resId))
    }

    override fun showError(message: String) {
        snackBar(
            message = message,
            view = binding.rootView
        )
    }
}