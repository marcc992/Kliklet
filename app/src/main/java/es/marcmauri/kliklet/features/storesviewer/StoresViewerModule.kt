package es.marcmauri.kliklet.features.storesviewer

import dagger.Module
import dagger.Provides
import es.marcmauri.kliklet.features.storesviewer.presenter.StoresViewerListPresenter

@Module
class StoresViewerModule {

    @Provides
    fun provideStoreViewerListPresenter(): StoresViewerListMVP.Presenter = StoresViewerListPresenter()
}