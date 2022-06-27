package es.marcmauri.kliklet.features.commercesviewer

import android.content.Context
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.CategoryInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface CommercesViewerListMVP {

    interface Model {
        suspend fun getAllCommerces(): List<Commerce>?
        suspend fun getCommercesByCategory(category: String): List<Commerce>?
        suspend fun getCommercesByDistance(kilometers: Int): List<Commerce>?
    }

    interface View {
        fun configureUI()
        fun showButtonList(newButtonsList: List<ButtonInfo>)
        fun showCategoryList(newCategoriesList: List<CategoryInfo>)
        fun showCommerceList(newCommerceList: List<Commerce>)
        fun changeSelectedButton(selectedBtnPosition: Int)
        fun changeButtonCount(btnPosition: Int, count: Int)
        fun changeSelectedCategory(selectedCategoryPosition: Int)
        fun goToCommerceDetails(commerce: Commerce)
        fun showLoading()
        fun hideLoading()
        fun showError(resId: Int)
        fun showError(message: String)
    }

    interface Presenter {
        fun setContext(ctx: Context)
        fun setView(view: View)
        fun onFragmentReady()
        fun onButtonItemClick(buttonInfo: ButtonInfo, position: Int)
        fun onCategoryItemClick(category: CategoryInfo, position: Int)
        fun onCommerceItemClick(commerce: Commerce)
    }
}