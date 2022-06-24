package es.marcmauri.kliklet.features.commercesviewer

import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface CommercesViewerListMVP {

    interface Model {
        suspend fun getAllCommerces(): List<Commerce>?
    }

    interface View {
        fun configureUI()
        fun showCommerceList(commerceList: List<Commerce>)
        fun goToCommerceDetails(commerce: Commerce)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun onFragmentReady()
        fun onCommerceItemClick(commerce: Commerce)
    }
}