package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiIpadPhotos (

  @SerializedName("welcome"  ) var apiWelcome  : ApiWelcome?            = ApiWelcome(),
  @SerializedName("rewards"  ) var apiRewards  : ApiRewards?            = ApiRewards(),
  @SerializedName("logo"     ) var apiLogo     : ApiLogo?               = ApiLogo(),
  @SerializedName("carousel" ) var apiCarousel : ArrayList<ApiCarousel> = arrayListOf()

)