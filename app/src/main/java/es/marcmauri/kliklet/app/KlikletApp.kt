package es.marcmauri.kliklet.app

import android.app.Application
import es.marcmauri.kliklet.features.stores.StoresModule

class KlikletApp : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
            .storesModule(StoresModule())
            .build()
    }

    fun getComponent() = component
}