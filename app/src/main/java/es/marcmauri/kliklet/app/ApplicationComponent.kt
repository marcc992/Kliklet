package es.marcmauri.kliklet.app

import dagger.Component
import es.marcmauri.kliklet.features.storesviewer.view.activity.StoresViewerActivity
import es.marcmauri.kliklet.features.storesviewer.StoresViewerModule
import es.marcmauri.kliklet.features.storesviewer.view.fragment.StoresViewerListFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        StoresViewerModule::class
    ]
)
interface ApplicationComponent {
    fun inject(storesViewerActivity: StoresViewerActivity)
    fun inject(storesViewerListFragment: StoresViewerListFragment)
}