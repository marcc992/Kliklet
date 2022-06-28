package es.marcmauri.kliklet.features.commercesviewer.view.listener

import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo

interface RecyclerButtonsViewerListListener {
    fun onButtonItemClick(buttonInfo: ButtonInfo, position: Int)
}