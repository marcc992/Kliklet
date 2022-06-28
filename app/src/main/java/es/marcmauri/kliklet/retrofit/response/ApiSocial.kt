package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiSocial (

  @SerializedName("twitter"   ) var twitter   : String? = null,
  @SerializedName("instagram" ) var instagram : String? = null,
  @SerializedName("facebook"  ) var facebook  : String? = null

)