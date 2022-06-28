package es.marcmauri.kliklet.features.commercesviewer.model.repository.impl

import es.marcmauri.kliklet.common.Constants
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Address
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Logo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Thumbnails
import es.marcmauri.kliklet.features.commercesviewer.model.repository.CommercesRepository
import es.marcmauri.kliklet.retrofit.CommercesApiService

class LiveCommercesRepositoryImpl(private val commercesApiService: CommercesApiService) :
    CommercesRepository {

    private var allCommercesFromApi: List<Commerce>? = null

    /**
     * Method to get all Commerces available on the server, which not includes the Commerces with
     * rare names.
     *
     * In first instance, no data exists in memory so the first time the app always will fetch all
     * the data from the server.
     *
     * In the next times this method is called, all the data will reside in a memory variable.
     * Because of this, to obtain this data will be time and network-free cost.
     */
    override suspend fun getAllCommerces(): List<Commerce> {
        if (allCommercesFromApi == null) {
            allCommercesFromApi =
                commercesApiService.getAllCommerces()
                    .filterNot { apiCommerce ->
                        // Filter the "invalid" commerces which should not be shown
                        apiCommerce.name.isNullOrEmpty()
                                || apiCommerce.name!!.lowercase().contains("* error")
                                || apiCommerce.name!!.lowercase().contains("*error")
                                || apiCommerce.name!!.lowercase().contains("* antiguo")
                                || apiCommerce.name!!.lowercase().contains("*antiguo")
                                || apiCommerce.name!!.lowercase().contains("* cerrado")
                                || apiCommerce.name!!.lowercase().contains("*cerrado")
                                || apiCommerce.name!!.lowercase().contains("* copy")
                                || apiCommerce.name!!.lowercase().contains("*copy")
                                || apiCommerce.name!!.lowercase().contains("copy*")
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
        }

        return allCommercesFromApi!!
    }
}