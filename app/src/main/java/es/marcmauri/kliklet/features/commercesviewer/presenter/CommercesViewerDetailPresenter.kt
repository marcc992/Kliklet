package es.marcmauri.kliklet.features.commercesviewer.presenter

import android.net.Uri
import androidx.annotation.Nullable
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerDetailMVP

class CommercesViewerDetailPresenter() :
    CommercesViewerDetailMVP.Presenter {

    @Nullable
    private var view: CommercesViewerDetailMVP.View? = null
    private val currentCommerce by lazy {
        view?.getCommerceFromExtras()
    }

    override fun setView(view: CommercesViewerDetailMVP.View) {
        this.view = view
    }

    override fun onFragmentReady() {
        view?.configureUI()
        getCommerceFromExtras()
    }

    private fun getCommerceFromExtras() {
        view?.showLoading()
        if (currentCommerce == null) {
            view?.showError("The selected commerce could not be loaded")
            view?.goToCommerceList()
        } else {
            view?.showCommerceDetails(currentCommerce!!)
        }
        view?.hideLoading()
    }

    override fun onBringMeThereButtonClick() {
        val gmmIntentUri =
            //Uri.parse("geo:0,0?q=${currentCommerce?.latitude},${currentCommerce?.longitude}")
            Uri.parse("google.navigation:q=${currentCommerce?.latitude},${currentCommerce?.longitude}") //Uri for navigation
        view?.goToGoogleMaps(gmmIntentUri)
    }

    override fun onBackButtonClick() {
        view?.goToCommerceList()
    }

}