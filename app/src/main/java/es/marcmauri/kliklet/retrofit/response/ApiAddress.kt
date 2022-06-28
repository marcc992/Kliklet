package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiAddress (

  @SerializedName("street"  ) var street  : String? = null,
  @SerializedName("country" ) var country : String? = null,
  @SerializedName("city"    ) var city    : String? = null,
  @SerializedName("zip"     ) var zip     : String?    = null

)