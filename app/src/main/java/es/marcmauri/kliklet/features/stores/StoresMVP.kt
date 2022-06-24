package es.marcmauri.kliklet.features.stores

interface StoresMVP {

    interface View {
        fun configureUI()
        fun showToast(text: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun onActivityReady()
    }
}