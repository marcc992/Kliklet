package es.marcmauri.kliklet.features.commercesviewer.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.common.distanceToInKm
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository

private const val TAG = "CommercesViewerListModel"

class CommercesViewerListModel(private val repository: CommercesRepository) :
    CommercesViewerListMVP.Model, Comparator<Commerce> {

    override suspend fun getAllCommerces(): List<Commerce>? {
        return try {
            repository.getAllCommerces()
                .sortedWith(this)
        } catch (e: Exception) {
            Log.e(TAG, "Something was wrong calling Commerces API -> ${e.localizedMessage}", e)
            null
        }
    }

    override suspend fun getCommercesByCategory(category: String): List<Commerce>? {
        return try {
            repository.getAllCommerces()
                .filter { commerce ->
                    commerce.category.trim().lowercase() == category.lowercase()
                }
                .sortedWith(this)
        } catch (e: Exception) {
            Log.e(TAG, "Something was wrong calling Commerces API -> ${e.localizedMessage}", e)
            null
        }
    }

    override suspend fun getCommercesByDistance(kilometers: Int): List<Commerce>? {
        if (!prefs.isLastLocationReady()) {
            return null
        }

        return try {
            repository.getAllCommerces()
                .filter { c ->
                    val commerceLocation = LatLng(c.latitude ?: 0.0, c.longitude ?: 0.0)
                    prefs.lastLocation.distanceToInKm(commerceLocation) <= kilometers
                }
                .sortedWith(this)
        } catch (e: Exception) {
            Log.e(TAG, "Something was wrong calling Commerces API -> ${e.localizedMessage}", e)
            null
        }
    }

    override fun compare(c0: Commerce?, c1: Commerce?): Int {
        if (!prefs.isLastLocationReady() || c0 == null || c1 == null) {
            return 0
        }

        val c0Location = LatLng(c0.latitude ?: 0.0, c0.longitude ?: 0.0)
        val c1Location = LatLng(c1.latitude ?: 0.0, c1.longitude ?: 0.0)
        return prefs.lastLocation.distanceToInKm(c0Location)
            .compareTo(prefs.lastLocation.distanceToInKm(c1Location))
    }
}