package es.marcmauri.kliklet.features.stores

import android.app.Activity
import android.os.Bundle
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.utils.toast
import javax.inject.Inject

class StoresActivity : Activity(), StoresMVP.View {

    @Inject
    lateinit var presenter:StoresMVP.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stores)

        (application as KlikletApp).getComponent().inject(this)

        presenter.setView(this)
        presenter.onActivityReady()
    }

    override fun configureUI() {
        // Nothing to do
    }

    override fun showToast(text: String) {
        toast(text)
    }
}