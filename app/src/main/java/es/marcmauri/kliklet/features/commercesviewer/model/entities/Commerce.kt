package es.marcmauri.kliklet.features.commercesviewer.model.entities

import es.marcmauri.kliklet.utils.Constants

data class Commerce(
    val name: String? = "Unknown",
    val description: String? = "Unknown",
    val category: String = Constants.Category.OTHER,
    val logo: Logo? = null,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0
)
