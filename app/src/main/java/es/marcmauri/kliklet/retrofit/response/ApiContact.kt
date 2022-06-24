package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiContact (

  @SerializedName("email" ) var email : String? = null,
  @SerializedName("phone" ) var phone : String? = null

)