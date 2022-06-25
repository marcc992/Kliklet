package es.marcmauri.kliklet.features.commercesviewer.model.entities

data class ButtonInfo(
    val description: String,
    val count: Int,
    var selected: Boolean = false
)
