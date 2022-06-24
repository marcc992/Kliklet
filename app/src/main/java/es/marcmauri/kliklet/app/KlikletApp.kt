package es.marcmauri.kliklet.app

import android.app.Application
import es.marcmauri.kliklet.features.storesviewer.StoresViewerModule

class KlikletApp : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
            .storesViewerModule(StoresViewerModule())
            .build()
    }

    fun getComponent() = component
}