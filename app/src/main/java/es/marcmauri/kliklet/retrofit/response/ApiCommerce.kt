package es.marcmauri.kliklet.retrofit.response

import com.google.gson.annotations.SerializedName


data class ApiCommerce (

  @SerializedName("slug"              ) var slug              : String?           = null,
  @SerializedName("address"           ) var apiAddress        : ApiAddress?       = ApiAddress(),
  @SerializedName("contact"           ) var apiContact        : ApiContact?       = ApiContact(),
  @SerializedName("social"            ) var apiSocial         : ApiSocial?        = ApiSocial(),
  @SerializedName("name"              ) var name              : String?           = null,
  @SerializedName("category"          ) var category          : String?           = null,
  @SerializedName("minLegalAge"       ) var minLegalAge       : Int?              = null,
  @SerializedName("shortDescription"  ) var shortDescription  : String?           = null,
  @SerializedName("description"       ) var description       : String?           = null,
  @SerializedName("openingHours"      ) var openingHours      : String?           = null,
  @SerializedName("ownerId"           ) var ownerId           : String?           = null,
  @SerializedName("contractId"        ) var contractId        : String?           = null,
  @SerializedName("pointsGroupId"     ) var pointsGroupId     : String?           = null,
  @SerializedName("franchiseId"       ) var franchiseId       : String?           = null,
  @SerializedName("config"            ) var apiConfig         : ApiConfig?        = ApiConfig(),
  @SerializedName("oldYoin"           ) var oldYoin           : Boolean?          = null,
  @SerializedName("whiteLabel"        ) var whiteLabel        : Boolean?          = null,
  @SerializedName("pos"               ) var pos               : Boolean?          = null,
  @SerializedName("polar"             ) var polar             : Boolean?          = null,
  @SerializedName("ipad"              ) var ipad              : Boolean?          = null,
  @SerializedName("surveyRequired"    ) var surveyRequired    : Boolean?          = null,
  @SerializedName("tags"              ) var tags              : ArrayList<String> = arrayListOf(),
  @SerializedName("features"          ) var features          : ArrayList<String> = arrayListOf(),
  @SerializedName("active"            ) var active            : Boolean?          = null,
  @SerializedName("whiteLabelGroupId" ) var whiteLabelGroupId : ArrayList<String> = arrayListOf(),
  @SerializedName("franchises"        ) var franchises        : ArrayList<String> = arrayListOf(),
  @SerializedName("ipadPhotos"        ) var apiIpadPhotos     : ApiIpadPhotos?    = ApiIpadPhotos(),
  @SerializedName("photos"            ) var photos            : ArrayList<ApiPhotos> = arrayListOf(),
  @SerializedName("__v"               ) var _v                : Int?              = null,
  @SerializedName("salesPerson"       ) var apiSalesPerson    : ApiSalesPerson?   = ApiSalesPerson(),
  @SerializedName("startDate"         ) var startDate         : String?           = null,
  @SerializedName("logo"              ) var apiLogo           : ApiLogo?          = ApiLogo(),
  @SerializedName("stuart"            ) var apiStuart         : ApiStuart?        = ApiStuart(),
  @SerializedName("id"                ) var id                : String?           = null,
  @SerializedName("latitude"          ) var latitude          : Double?           = null,
  @SerializedName("longitude"         ) var longitude         : Double?           = null

)