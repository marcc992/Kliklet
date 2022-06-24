package es.marcmauri.kliklet.features.commercesviewer.model.repository

import es.marcmauri.kliklet.retrofit.response.ApiCommerce

interface CommercesRepository {
    suspend fun getAllCommerces(): List<ApiCommerce>
}