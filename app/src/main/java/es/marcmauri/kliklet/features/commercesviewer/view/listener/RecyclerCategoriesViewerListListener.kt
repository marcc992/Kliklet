package es.marcmauri.kliklet.features.commercesviewer.view.listener

import es.marcmauri.kliklet.features.commercesviewer.model.entities.CategoryInfo

interface RecyclerCategoriesViewerListListener {
    fun onCategoryItemClick(category: CategoryInfo, position: Int)
}