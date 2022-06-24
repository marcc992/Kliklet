package es.marcmauri.kliklet.features.stores

import dagger.Module
import dagger.Provides

@Module
class StoresModule {

    @Provides
    fun provideStoresPresenter(): StoresMVP.Presenter = StoresPresenter()
}