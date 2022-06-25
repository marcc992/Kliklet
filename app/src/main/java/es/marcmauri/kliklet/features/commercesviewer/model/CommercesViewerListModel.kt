package es.marcmauri.kliklet.features.commercesviewer.model

import android.util.Log
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Address
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Logo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Thumbnails
import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository
import es.marcmauri.kliklet.utils.Constants

private const val TAG = "CommercesViewerListModel"

class CommercesViewerListModel(private val repository: CommercesRepository) :
    CommercesViewerListMVP.Model {

    override suspend fun getAllCommerces(): List<Commerce>? {
        return try {
            repository.getAllCommerces()
                .filterNot { apiCommerce ->
                    // Filter the "invalid" commerces which should not be shown
                    apiCommerce.name.isNullOrEmpty()
                            || apiCommerce.name!!.lowercase().contains("* error")
                            || apiCommerce.name!!.lowercase().contains("*error")
                            || apiCommerce.name!!.lowercase().contains("* antiguo")
                            || apiCommerce.name!!.lowercase().contains("*antiguo")
                            || apiCommerce.name!!.lowercase().contains("* cerrado")
                            || apiCommerce.name!!.lowercase().contains("*cerrado")
                }
                .map { apiCommerce ->
                    Commerce(
                        apiCommerce.name,
                        apiCommerce.description,
                        apiCommerce.category ?: Constants.Category.OTHER,
                        Address(
                            apiCommerce.apiAddress?.street,
                            apiCommerce.apiAddress?.country,
                            apiCommerce.apiAddress?.city,
                            apiCommerce.apiAddress?.zip
                        ),
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
            Log.e(TAG, "Something was wrong calling Commerces API -> ${e.localizedMessage}", e)
            null
        }
    }
}