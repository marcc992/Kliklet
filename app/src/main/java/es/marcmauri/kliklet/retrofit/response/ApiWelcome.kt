package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiWelcome (

    @SerializedName("thumbnails" ) var apiThumbnails : ApiThumbnails? = ApiThumbnails(),
    @SerializedName("format"     ) var format     : String?     = null,
    @SerializedName("url"        ) var url        : String?     = null

)