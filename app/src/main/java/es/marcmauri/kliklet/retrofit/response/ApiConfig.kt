package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiConfig (

  @SerializedName("timezone" ) var timezone : String? = null,
  @SerializedName("currency" ) var currency : String? = null,
  @SerializedName("locale"   ) var locale   : String? = null

)