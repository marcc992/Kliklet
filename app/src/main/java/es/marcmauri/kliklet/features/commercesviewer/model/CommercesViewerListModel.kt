package es.marcmauri.kliklet.features.commercesviewer.model

import android.util.Log
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Logo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Thumbnails
import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository
import es.marcmauri.kliklet.utils.Constants
import java.lang.Exception

private const val TAG = "CommercesViewerListModel"

class CommercesViewerListModel(private val repository: CommercesRepository) :
    CommercesViewerListMVP.Model {

    override suspend fun getAllCommerces(): List<Commerce>? {
        return try {
            repository.getAllCommerces().map { apiCommerce ->
                Commerce(
                    apiCommerce.name,
                    apiCommerce.description,
                    apiCommerce.category?: Constants.Category.OTHER,
                    Logo(
                        Thumbnails(
                            apiCommerce.apiLogo?.apiThumbnails?.small,
                            apiCommerce.apiLogo?.apiThumbnails?.medium,
                            apiCommerce.apiLogo?.apiThumbnails?.large
                        ),
                        apiCommerce.apiLogo?.format,
                        apiCommerce.apiLogo?.url
                    ),
                    apiCommerce.latitude,
                    apiCommerce.longitude
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Something was wrong calling Commerces API /GET all -> ${e.localizedMessage}", e)
            null
        }
    }
}