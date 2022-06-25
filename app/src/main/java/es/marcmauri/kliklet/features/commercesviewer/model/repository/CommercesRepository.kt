package es.marcmauri.kliklet.features.commercesviewer.model.repository

import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface CommercesRepository {
    suspend fun getAllCommerces(): List<Commerce>
}