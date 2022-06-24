package es.marcmauri.kliklet.app

import android.app.Application
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerModule
import es.marcmauri.kliklet.retrofit.CommercesApiModule

class KlikletApp : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
            .commercesViewerModule(CommercesViewerModule())
            .commercesApiModule(CommercesApiModule())
            .build()
    }

    fun getComponent() = component
}