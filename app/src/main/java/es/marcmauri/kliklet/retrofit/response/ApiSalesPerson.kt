package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiSalesPerson (

  @SerializedName("email" ) var email : String? = null,
  @SerializedName("name"  ) var name  : String? = null

)