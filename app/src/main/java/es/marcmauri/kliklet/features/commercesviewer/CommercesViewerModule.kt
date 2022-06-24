package es.marcmauri.kliklet.features.commercesviewer

import dagger.Module
import dagger.Provides
import es.marcmauri.kliklet.features.commercesviewer.model.CommercesViewerListModel
import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository
import es.marcmauri.kliklet.features.commercesviewer.model.repository.impl.LiveCommercesRepositoryImpl
import es.marcmauri.kliklet.features.commercesviewer.presenter.CommercesViewerListPresenter
import es.marcmauri.kliklet.retrofit.CommercesApiService
import javax.inject.Singleton

@Module
class CommercesViewerModule {

    @Provides
    fun provideCommercesViewerListPresenter(model: CommercesViewerListMVP.Model): CommercesViewerListMVP.Presenter =
        CommercesViewerListPresenter(model)

    @Provides
    fun provideCommercesViewerListModel(repository: CommercesRepository): CommercesViewerListMVP.Model =
        CommercesViewerListModel(repository)

    @Singleton
    @Provides
    fun provideCommercesRepository(commercesApiService: CommercesApiService): CommercesRepository =
        LiveCommercesRepositoryImpl(commercesApiService)
}