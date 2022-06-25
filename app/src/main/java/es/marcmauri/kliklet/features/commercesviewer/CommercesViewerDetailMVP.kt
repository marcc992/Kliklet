package es.marcmauri.kliklet.features.commercesviewer

import android.net.Uri
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface CommercesViewerDetailMVP {

    interface View {
        fun configureUI()
        fun getCommerceFromExtras(): Commerce?
        fun showCommerceDetails(commerce: Commerce)
        fun goToGoogleMaps(uri: Uri)
        fun goToCommerceList()
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun onFragmentReady()
        fun onBringMeThereButtonClick()
        fun onBackButtonClick()
    }
}