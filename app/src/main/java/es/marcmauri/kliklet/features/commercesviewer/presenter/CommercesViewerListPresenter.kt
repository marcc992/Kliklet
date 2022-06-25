package es.marcmauri.kliklet.features.commercesviewer.presenter

import android.content.Context
import androidx.annotation.Nullable
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerListMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommercesViewerListPresenter(val model: CommercesViewerListMVP.Model) :
    CommercesViewerListMVP.Presenter {

    @Nullable
    private var context: Context? = null
    @Nullable
    private var view: CommercesViewerListMVP.View? = null

    override fun setContext(ctx: Context) {
        context = ctx
    }

    override fun setView(view: CommercesViewerListMVP.View) {
        this.view = view
    }

    override fun onFragmentReady() {
        view?.configureUI()
        getCommercesFromAPI()
    }

    private fun getButtonInfoList(commerceList: List<Commerce>?): List<ButtonInfo> {
        val buttonInfoList = ArrayList<ButtonInfo>(0)

        // Button to show all
        buttonInfoList.add(
            ButtonInfo(
                context!!.getString(R.string.recycler_view_button_info_commerces),
                commerceList?.size ?: 0,
                true
            )
        )

        // TODO: Determinar los comercios a menos de 10 km
        buttonInfoList.add(
            ButtonInfo(
                context!!.getString(R.string.recycler_view_button_info_less_distance),
                123
            )
        )

        return buttonInfoList
    }

    private fun getCategoryList(commerceList: List<Commerce>?): List<String> {
        val categoriesMap = HashMap<String, Boolean>(0)
        commerceList?.forEach { categoriesMap[it.category] = true}
        return categoriesMap.keys.toList()
    }

    private fun getCommercesFromAPI() {
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

    override fun onButtonItemClick(buttonInfo: ButtonInfo) {
        view?.showError("TODO(\"onButtonItemClick Not yet implemented\")")
    }

    override fun onCategoryItemClick(category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                view?.showLoading()
            }

            // Background work
            val commerceList = model.getCommercesByCategory(category)

            withContext(Dispatchers.Main) {
                view?.let { v ->
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