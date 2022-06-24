package es.marcmauri.kliklet.features.stores

import androidx.annotation.Nullable

class StoresPresenter : StoresMVP.Presenter {

    @Nullable
    private var view: StoresMVP.View? = null

    override fun setView(view: StoresMVP.View) {
        this.view = view
    }

    override fun onActivityReady() {
        view?.showToast("The view is ready!")
    }
}