package es.marcmauri.kliklet.features.storesviewer.presenter

import androidx.annotation.Nullable
import es.marcmauri.kliklet.features.storesviewer.StoresViewerListMVP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoresViewerListPresenter : StoresViewerListMVP.Presenter {

    @Nullable
    private var view: StoresViewerListMVP.View? = null

    override fun setView(view: StoresViewerListMVP.View) {
        this.view = view
    }

    override fun onFragmentReady() {
        view?.configureUI()
        getStores()
    }

    private fun getStores() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                view?.showLoading()
            }

            // Bakcground work
            val stores = ArrayList<String>()
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")
            stores.add("ABC")

            withContext(Dispatchers.Main) {
                view?.let { v ->
                    when {
                        stores == null -> v.showError("An error occurs fetching data from server")
                        stores.isEmpty() -> v.showError("No stores available right now")
                        else -> v.showStoreList(stores)
                    }
                    v.hideLoading()
                }
            }
        }

    }

    override fun onStoreItemClick(todo: String) {
        view?.goToStoreDetails(todo)
    }


}