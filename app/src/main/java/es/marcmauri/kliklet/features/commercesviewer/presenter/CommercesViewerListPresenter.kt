package es.marcmauri.kliklet.features.commercesviewer.presenter

import android.content.Context
import androidx.annotation.Nullable
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.common.Constants.Literals.Companion.EMPTY
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
private const val POS_BUTTON_FIRST = 0
private const val UNSELECTED_CATEGORY = -1

class CommercesViewerListPresenter(val model: CommercesViewerListMVP.Model) :
    CommercesViewerListMVP.Presenter {

    enum class CommerceRequestType {
        ALL, NEAREST, CATEGORY
    }

    @Nullable
    private var context: Context? = null

    @Nullable
    private var view: CommercesViewerListMVP.View? = null
    private var selectedButton = 0 // The first button is selected on init
    private var selectedCategory = UNSELECTED_CATEGORY // No category button selected on init
    private var allCommercesCount = 0 // This var is updated the first time we ask for commerces
    private var allCommercesByDistanceCount = 0 // This var is asked on init, needed for 2nd button
    private var buttonInfoList = ArrayList<ButtonInfo>(0)
    private var categoryInfoList = ArrayList<CategoryInfo>(0)


    /**
     * Setter of the context. It has to be used in this presenter.
     *
     * @param ctx: Application context
     */
    override fun setContext(ctx: Context) {
        context = ctx
    }

    /**
     * Setter of the view.
     */
    override fun setView(view: CommercesViewerListMVP.View) {
        this.view = view
    }

    /**
     * Funct called when the Fragment is ready to configure the next steps.
     */
    override fun onFragmentReady() {
        view?.configureUI()
        showInitDisplayData()
    }

    /**
     * This suspend funct is called once to configure the two buttons.
     * It gets info about all commerces and near to the user to populate the buttons.
     */
    private suspend fun initButtonInfoList() {
        allCommercesCount = model.getAllCommerces()?.size ?: 0
        allCommercesByDistanceCount = model.getCommercesByDistance(DEFAULT_DISTANCE)?.size ?: 0

        buttonInfoList.add(
            ButtonInfo(
                description = context!!.getString(R.string.recycler_view_button_info_commerces),
                count = allCommercesCount,
                selected = true
            )
        )

        buttonInfoList.add(
            ButtonInfo(
                description = context!!.getString(R.string.recycler_view_button_info_less_distance),
                count = allCommercesByDistanceCount,
                selected = false
            )
        )
    }

    /**
     * This funct uses the commerce list passes by params to determine all the different categories
     * available.
     * For now, it is called once when the init display data is loading.
     *
     * @param commerceList A nullable list of Commerce. In case of null or emptly the categories
     * found will be 0.
     *
     * @return It returns an ArrayList which contains all the unique Categories found.
     */
    private fun getCategoryInfoList(commerceList: List<Commerce>?) =
        commerceList
            ?.map { commerce -> commerce.category }
            ?.distinct()
            ?.map { categoryName ->
                CategoryInfo(
                    name = categoryName,
                    selected = false
                )
            } ?: ArrayList(0)

    /**
     * This funct uses a coroutine to be allowed to fetch all the requiered data from server (or
     * memory en case the repository has been retreived the data before) for the initial display
     * data which will be showed at first.
     */
    private fun showInitDisplayData() {
        CoroutineScope(IO).launch {
            withContext(Main) {
                view?.showLoading()
            }

            val allCommerces = model.getAllCommerces()
            initButtonInfoList()
            categoryInfoList.addAll(getCategoryInfoList(allCommerces))

            withContext(Main) {
                view?.let { v ->
                    v.showButtonList(buttonInfoList)
                    v.showCategoryList(categoryInfoList)
                    if (allCommerces.isNullOrEmpty()) v.showError(R.string.error_message_no_commerces_available)
                    else v.showCommerceList(allCommerces)
                    v.hideLoading()
                }
            }
        }
    }

    /**
     * This funct uses a coroutine to get the requested commerces by the user, either ALL, the
     * NEAREST or by CATEGORY. Depends on what kind of data is requested, the buttons and
     * categories will be set properly
     *
     * @param type: Kind of commerces the user wants to obtain about enum CommerceRequestType
     * @param distance: (Optional) In case of NEAREST commerces, its radius of search.
     * @param category: (Optional) But needed when Commerces by CATEGORY are requested
     */
    private fun getCommercesFromRepository(
        type: CommerceRequestType,
        distance: Int = DEFAULT_DISTANCE,
        category: String = EMPTY
    ) {
        CoroutineScope(IO).launch {
            withContext(Main) {
                view?.showLoading()
            }

            val commerceList = when (type) {
                CommerceRequestType.ALL -> model.getAllCommerces()
                CommerceRequestType.NEAREST -> model.getCommercesByDistance(distance)
                CommerceRequestType.CATEGORY -> model.getCommercesByCategory(category)
            }

            withContext(Main) {
                view?.let { v ->
                    if (commerceList.isNullOrEmpty()) v.showError(R.string.error_message_no_commerces_available)
                    else {
                        v.showCommerceList(commerceList)
                        v.changeButtonCount(
                            POS_BUTTON_FIRST,
                            when (type) {
                                CommerceRequestType.CATEGORY -> commerceList.size
                                else -> allCommercesCount
                            }
                        )
                    }
                    v.hideLoading()
                }
            }
        }
    }

    /**
     * This funct is called when a button has clicked
     *
     * @param buttonInfo: The button clicked
     * @param position: The position of the button in its recycler view
     */
    override fun onButtonItemClick(buttonInfo: ButtonInfo, position: Int) {
        if (selectedButton != position) {
            selectedButton = position
            view?.changeSelectedButton(selectedButton)

            when (position) {
                1 -> {
                    if (prefs.isLastLocationReady()) {
                        // On nearest commerces button click, the selected category is unselected
                        selectedCategory = UNSELECTED_CATEGORY
                        view?.changeSelectedCategory(selectedCategory)
                        // Then get the requested data
                        getCommercesFromRepository(type = CommerceRequestType.NEAREST)
                    } else {
                        // Location disabled!
                        view?.showError(R.string.error_message_location_disabled)
                    }
                }
                else -> {
                    getCommercesFromRepository(type = CommerceRequestType.ALL)
                }
            }
        } // else Do nothing because we are clicking on the same button
    }

    /**
     * This funct is called when a category has clicked
     *
     * @param category: The category clicked
     * @param position: The position of the category in its recycler view
     */
    override fun onCategoryItemClick(category: CategoryInfo, position: Int) {
        // On category click, the button selected will be always the first
        if (selectedButton != POS_BUTTON_FIRST) {
            selectedButton = POS_BUTTON_FIRST
            view?.changeSelectedButton(selectedButton)
        }

        if (selectedCategory != position) {
            selectedCategory = position
            view?.changeSelectedCategory(selectedCategory)

            getCommercesFromRepository(
                type = CommerceRequestType.CATEGORY,
                category = category.name
            )
        } else {
            // If the user clicks again on the same category, then unselect it and load all commerces
            selectedCategory = UNSELECTED_CATEGORY
            view?.changeSelectedCategory(selectedCategory)

            getCommercesFromRepository(type = CommerceRequestType.ALL)
        }
    }

    /**
     * This funct call the view method to open the detail of the selected Commerce
     *
     * @param commerce: Selected commerce by user
     */
    override fun onCommerceItemClick(commerce: Commerce) {
        view?.goToCommerceDetails(commerce)
    }

}