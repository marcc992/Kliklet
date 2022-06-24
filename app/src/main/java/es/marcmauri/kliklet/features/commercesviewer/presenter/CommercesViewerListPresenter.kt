package es.marcmauri.kliklet.features.commercesviewer.presenter

import androidx.annotation.Nullable
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommercesViewerListPresenter(val model: CommercesViewerListMVP.Model) :
    CommercesViewerListMVP.Presenter {

    @Nullable
    private var view: CommercesViewerListMVP.View? = null

    override fun setView(view: CommercesViewerListMVP.View) {
        this.view = view
    }

    override fun onFragmentReady() {
        view?.configureUI()
        getCommerces()
    }

    private fun getCommerces() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getAllCommerces()

            withContext(Dispatchers.Main) {
                view?.let { v ->
                    v.hideLoading()
                    when {
                        commerceList == null -> v.showError("An error occurs fetching data from server")
                        commerceList.isEmpty() -> v.showError("No commerces available right now")
                        else -> v.showCommerceList(commerceList)
                    }
                }
            }
        }

    }

    override fun onCommerceItemClick(commerce: Commerce) {
        view?.goToCommerceDetails(commerce)
    }


}