package es.marcmauri.kliklet.features.storesviewer

interface StoresViewerListMVP {

    interface View {
        fun configureUI()
        fun showStoreList(todo: List<String>)
        fun goToStoreDetails(todo: String)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun onFragmentReady()
        fun onStoreItemClick(todo: String)
    }
}