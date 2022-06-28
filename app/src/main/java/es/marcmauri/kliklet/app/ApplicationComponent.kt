package es.marcmauri.kliklet.app

import dagger.Component
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerModule
import es.marcmauri.kliklet.features.commercesviewer.view.activity.CommercesViewerActivity
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerDetailFragment
import es.marcmauri.kliklet.features.commercesviewer.view.fragment.CommercesViewerListFragment
import es.marcmauri.kliklet.retrofit.CommercesApiModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        CommercesViewerModule::class,
        CommercesApiModule::class
    ]
)
interface ApplicationComponent {
    fun inject(commercesViewerActivity: CommercesViewerActivity)
    fun inject(commercesViewerListFragment: CommercesViewerListFragment)
    fun inject(commercesViewerDetailFragment: CommercesViewerDetailFragment)
}