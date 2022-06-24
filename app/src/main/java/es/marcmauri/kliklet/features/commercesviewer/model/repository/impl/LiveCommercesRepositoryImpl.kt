package es.marcmauri.kliklet.features.commercesviewer.model.repository.impl

import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository
import es.marcmauri.kliklet.retrofit.CommercesApiService
import es.marcmauri.kliklet.retrofit.response.ApiCommerce

class LiveCommercesRepositoryImpl(private val commercesApiService: CommercesApiService) :
    CommercesRepository {

    override suspend fun getAllCommerces(): List<ApiCommerce> =
        commercesApiService.getAllCommerces()
}