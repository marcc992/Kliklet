package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiCarousel (

  @SerializedName("_id"        ) var Id         : String?     = null,
  @SerializedName("format"     ) var format     : String?     = null,
  @SerializedName("url"        ) var url        : String?     = null,
  @SerializedName("thumbnails" ) var apiThumbnails : ApiThumbnails? = ApiThumbnails()

)