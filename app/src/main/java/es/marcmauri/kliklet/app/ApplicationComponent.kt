package es.marcmauri.kliklet.app

import dagger.Component
import es.marcmauri.kliklet.features.stores.StoresActivity
import es.marcmauri.kliklet.features.stores.StoresModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        StoresModule::class
    ]
)
interface ApplicationComponent {
    fun inject(storesActivity: StoresActivity)
}