package es.marcmauri.kliklet.features.commercesviewer.model

import android.util.Log
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository

private const val TAG = "CommercesViewerListModel"

class CommercesViewerListModel(private val repository: CommercesRepository) :
    CommercesViewerListMVP.Model {

    override suspend fun getAllCommerces(): List<Commerce>? {
        return try {
            repository.getAllCommerces()
        } catch (e: Exception) {
            Log.e(TAG, "Something was wrong calling Commerces API -> ${e.localizedMessage}", e)
            null
        }
    }

    override suspend fun getCommercesByCategory(category: String): List<Commerce>? {
        return try {
            repository.getAllCommerces().filter { commerce ->
                commerce.category.trim().lowercase() == category.lowercase()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Something was wrong calling Commerces API -> ${e.localizedMessage}", e)
            null
        }
    }

    override suspend fun getCommercesByDistance(meters: Int): List<Commerce>? {
        TODO("Not yet implemented")
    }
}