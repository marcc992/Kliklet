package es.marcmauri.kliklet.features.commercesviewer.view.listener

import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface RecyclerCategoriesViewerListListener {
    fun onPhotoItemClick(commerce: Commerce, position: Int)
}