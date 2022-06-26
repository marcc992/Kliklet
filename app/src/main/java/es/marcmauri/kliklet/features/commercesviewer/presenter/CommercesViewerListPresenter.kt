package es.marcmauri.kliklet.features.commercesviewer.presenter

import android.content.Context
import androidx.annotation.Nullable
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private suspend fun getButtonInfoList(commerceList: List<Commerce>?): List<ButtonInfo> {
        val buttonInfoList = ArrayList<ButtonInfo>(0)

        // Button to show all
        buttonInfoList.add(
            ButtonInfo(
                context!!.getString(R.string.recycler_view_button_info_commerces),
                commerceList?.size ?: 0,
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

    private fun getCategoryList(commerceList: List<Commerce>?): List<String> {
        val categoriesMap = HashMap<String, Boolean>(0)
        commerceList?.forEach { categoriesMap[it.category] = true }
        return categoriesMap.keys.toList()
    }

    private fun getAllCommerces() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getAllCommerces()
            val buttonList = getButtonInfoList(commerceList)
            val categoryList = getCategoryList(commerceList)

            withContext(Dispatchers.Main) {
                view?.let { v ->
                    v.showButtonList(buttonList)
                    v.showCategoryList(categoryList)
                    v.hideLoading()
                    when {
                        commerceList == null -> v.showError("An error occurs fetching data from server")
                        commerceList.isEmpty() -> v.showError("No commerces available right now")
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
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getCommercesByDistance(DEFAULT_DISTANCE)
            val buttonList = getButtonInfoList(commerceList)

            withContext(Dispatchers.Main) {
                view?.let { v ->
                    v.showButtonList(buttonList)
                    v.hideLoading()
                    when {
                        commerceList == null -> v.showError("An error occurs fetching data from server")
                        commerceList.isEmpty() -> v.showError("No commerces available around you right now")
                        else -> v.showCommerceList(commerceList)
                    }
                }
            }
        }
    }

    override fun onCategoryItemClick(category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getCommercesByCategory(category)
            selectedButton = 0 // Go to the first button which shows the commerces by category count
            val buttonList = getButtonInfoList(commerceList)

            withContext(Dispatchers.Main) {
                view?.let { v ->
                    v.showButtonList(buttonList)
                    v.hideLoading()
                    when {
                        commerceList == null -> v.showError("An error occurs fetching data from server")
                        commerceList.isEmpty() -> v.showError("No $category commerces available right now")
                        else -> v.showCommerceList(commerceList)
                    }
                }
            }
        }
    }

    override fun onCommerceItemClick(commerce: Commerce) {
        view?.goToCommerceDetails(commerce)
    }


}