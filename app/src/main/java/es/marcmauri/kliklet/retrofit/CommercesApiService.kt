package es.marcmauri.kliklet.retrofit

import es.marcmauri.kliklet.retrofit.response.ApiCommerce
import retrofit2.http.GET

interface CommercesApiService {

    @GET("commerces/public")
    suspend fun getAllCommerces(): List<ApiCommerce>
}