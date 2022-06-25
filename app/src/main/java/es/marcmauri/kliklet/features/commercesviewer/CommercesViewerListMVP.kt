package es.marcmauri.kliklet.features.commercesviewer

import android.content.Context
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface CommercesViewerListMVP {

    interface Model {
        suspend fun getAllCommerces(): List<Commerce>?
        suspend fun getCommercesByCategory(category: String): List<Commerce>?
        suspend fun getCommercesByDistance(meters: Int): List<Commerce>?
    }

    interface View {
        fun configureUI()
        fun showButtonList(newButtonsList: List<ButtonInfo>)
        fun showCategoryList(newCategoriesList: List<String>)
        fun showCommerceList(newCommerceList: List<Commerce>)
        fun goToCommerceDetails(commerce: Commerce)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun setContext(ctx: Context)
        fun setView(view: View)
        fun onFragmentReady()
        fun onButtonItemClick(buttonInfo: ButtonInfo)
        fun onCategoryItemClick(category: String)
        fun onCommerceItemClick(commerce: Commerce)
    }
}