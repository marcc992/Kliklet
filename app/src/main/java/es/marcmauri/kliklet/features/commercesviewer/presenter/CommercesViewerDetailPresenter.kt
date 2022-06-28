package es.marcmauri.kliklet.features.commercesviewer.presenter

import android.net.Uri
import androidx.annotation.Nullable
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerDetailMVP

class CommercesViewerDetailPresenter :
    CommercesViewerDetailMVP.Presenter {

    @Nullable
    private var view: CommercesViewerDetailMVP.View? = null
    private val currentCommerce by lazy {
        view?.getCommerceFromExtras()
    }

    /**
     * Setter of the view.
     */
    override fun setView(view: CommercesViewerDetailMVP.View) {
        this.view = view
    }

    /**
     * Method called when the Fragment is ready to configure the next steps.
     */
    override fun onFragmentReady() {
        view?.configureUI()
        getCommerceFromExtras()
    }

    /**
     * Method called to get the selected Commerce by user to show its detail.
     * If something goes wrong, an error is shown and the app returns to the previous list
     */
    private fun getCommerceFromExtras() {
        view?.showLoading()
        if (currentCommerce == null) {
            view?.showError(R.string.error_message_commerce_not_loaded)
            view?.goToCommerceList()
        } else {
            view?.showCommerceDetails(currentCommerce!!)
        }
        view?.hideLoading()
    }

    /**
     * This function is called when the users clicks on the custom button to start the
     * GPS Navigation to the selected Commerce.
     */
    override fun onBringMeThereButtonClick() {
        //Uri for navigation
        val gmmIntentUri =
            Uri.parse("google.navigation:q=${currentCommerce?.latitude},${currentCommerce?.longitude}")
        view?.goToGoogleMaps(gmmIntentUri)
    }

}