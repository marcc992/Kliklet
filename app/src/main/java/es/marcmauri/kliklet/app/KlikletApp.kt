package es.marcmauri.kliklet.app

import android.app.Application
import android.content.Context
import es.marcmauri.kliklet.common.SharedPreferencesManager
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerModule
import es.marcmauri.kliklet.retrofit.CommercesApiModule

val prefs: SharedPreferencesManager by lazy { KlikletApp.prefs!! }

class KlikletApp : Application() {

    companion object {
        var prefs: SharedPreferencesManager? = null
    }

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        prefs = SharedPreferencesManager(applicationContext)
        component = DaggerApplicationComponent.builder()
            .commercesViewerModule(CommercesViewerModule())
            .commercesApiModule(CommercesApiModule())
            .build()
    }

    fun getComponent() = component
}