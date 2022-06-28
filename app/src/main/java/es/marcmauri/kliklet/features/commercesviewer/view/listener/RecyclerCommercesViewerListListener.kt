package es.marcmauri.kliklet.features.commercesviewer.view.listener

import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce

interface RecyclerCommercesViewerListListener {
    fun onCommerceItemClick(commerce: Commerce)
}