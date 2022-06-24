package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiThumbnails (

  @SerializedName("small"  ) var small  : String? = null,
  @SerializedName("medium" ) var medium : String? = null,
  @SerializedName("large"  ) var large  : String? = null

)