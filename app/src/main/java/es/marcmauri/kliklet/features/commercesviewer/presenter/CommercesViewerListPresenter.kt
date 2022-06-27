package es.marcmauri.kliklet.features.commercesviewer.presenter

import android.content.Context
import androidx.annotation.Nullable
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.CategoryInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val DEFAULT_DISTANCE = 10

class CommercesViewerListPresenter(val model: CommercesViewerListMVP.Model) :
    CommercesViewerListMVP.Presenter {

    @Nullable
    private var context: Context? = null

    @Nullable
    private var view: CommercesViewerListMVP.View? = null
    private var selectedButton = 0
    private var selectedCategory = -1
    private var allCommercesCount = 0

    override fun setContext(ctx: Context) {
        context = ctx
    }

    override fun setView(view: CommercesViewerListMVP.View) {
        this.view = view
    }

    override fun onFragmentReady() {
        view?.configureUI()
        getAllCommerces()
    }

    private suspend fun getButtonInfoList(commercesCount: Int): List<ButtonInfo> {
        val buttonInfoList = ArrayList<ButtonInfo>(0)

        // Button to show all
        buttonInfoList.add(
            ButtonInfo(
                context!!.getString(R.string.recycler_view_button_info_commerces),
                commercesCount,
                selectedButton == 0
            )
        )

        buttonInfoList.add(
            ButtonInfo(
                context!!.getString(R.string.recycler_view_button_info_less_distance),
                model.getCommercesByDistance(DEFAULT_DISTANCE)?.size ?: 0,
                selectedButton == 1
            )
        )

        return buttonInfoList
    }

    private fun getCategoryInfoList(commerceList: List<Commerce>?): List<CategoryInfo> {
        return commerceList
            ?.map { commerce -> commerce.category }
            ?.distinct()
            ?.mapIndexed { position, categoryName ->
                CategoryInfo(
                    categoryName,
                    selectedCategory == position
                )
            } ?: ArrayList(0)
    }

    private fun getAllCommerces() {
        // Unselect any category
        selectedCategory = -1

        CoroutineScope(IO).launch {
            withContext(Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getAllCommerces()
            allCommercesCount = commerceList?.size ?: 0

            val buttonList = getButtonInfoList(allCommercesCount)
            val categoryList = getCategoryInfoList(commerceList) // todo: cargar a cada rato?

            withContext(Main) {
                view?.let { v ->
                    v.showButtonList(buttonList)
                    v.showCategoryList(categoryList) // todo: cargar a cada rato? volver al inicio?
                    v.hideLoading()
                    when {
                        commerceList.isNullOrEmpty() -> v.showError("No commerces available right now")
                        else -> v.showCommerceList(commerceList)
                    }
                }
            }
        }

    }

    override fun onButtonItemClick(buttonInfo: ButtonInfo, position: Int) {
        if (selectedButton != position) {
            selectedButton = position

            when (position) {
                1 -> {
                    if (prefs.isLastLocationReady()) {
                        getCommercesNextToUser()
                    } else {
                        // Location disabled! Show an alert to user...
                        view?.showError("You can't use this option with location disabled...")
                    }
                }
                else -> {
                    getAllCommerces()
                }
            }
        } // else Do nothing because we are clicking on the same button
    }

    private fun getCommercesNextToUser() {
        // Unselect any category
        selectedCategory = -1

        CoroutineScope(IO).launch {
            withContext(Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getCommercesByDistance(DEFAULT_DISTANCE)
            val categoryList = getCategoryInfoList(model.getAllCommerces()) // todo: a lo guarro! cargar a cada rato?
            val buttonList = getButtonInfoList(allCommercesCount)

            withContext(Main) {
                view?.let { v ->
                    v.showButtonList(buttonList)
                    v.showCategoryList(categoryList)
                    v.hideLoading()
                    when {
                        commerceList.isNullOrEmpty() -> v.showError("No commerces available around you right now")
                        else -> v.showCommerceList(commerceList)
                    }
                }
            }
        }
    }

    override fun onCategoryItemClick(category: CategoryInfo, position: Int) {
        if (selectedCategory != position) {
            selectedCategory = position

            CoroutineScope(IO).launch {
                withContext(Main) {
                    view?.showLoading()
                }

                // Background work
                val commerceList = model.getCommercesByCategory(category.name)
                selectedButton = 0 // Mark first button as selected when we are on a category
                val categoryList = getCategoryInfoList(model.getAllCommerces()) // todo: muy guarro
                val buttonList = getButtonInfoList(commerceList?.size ?: 0)

                withContext(Main) {
                    view?.let { v ->
                        v.showButtonList(buttonList)
                        v.showCategoryList(categoryList)
                        v.hideLoading()
                        when {
                            commerceList.isNullOrEmpty() -> v.showError("No $category commerces available right now")
                            else -> v.showCommerceList(commerceList)
                        }
                    }
                }
            }
        } else {
            // If the user clicks again on the same category, then unselect it and load all commerces again
            getAllCommerces()
        }
    }

    override fun onCommerceItemClick(commerce: Commerce) {
        view?.goToCommerceDetails(commerce)
    }


}