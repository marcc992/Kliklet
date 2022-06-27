package es.marcmauri.kliklet.features.commercesviewer.view.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.common.Constants
import es.marcmauri.kliklet.common.asSentence
import es.marcmauri.kliklet.databinding.RecyclerCategoryListItemBinding
import es.marcmauri.kliklet.features.commercesviewer.model.entities.CategoryInfo
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCategoriesViewerListListener


class CategoriesViewerListAdapter(
    private val categoryList: List<CategoryInfo>,
    private val listener: RecyclerCategoriesViewerListListener
) : RecyclerView.Adapter<CategoriesViewerListAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            RecyclerCategoryListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    private fun getCategorySymbol(category: CategoryInfo): Drawable =
        when (category.name) {
            Constants.Category.BEAUTY ->
                AppCompatResources.getDrawable(
                    context,
                    if (!category.selected) R.drawable.ic_car_wash_colour else R.drawable.ic_car_wash_white
                )!!
            Constants.Category.FOOD ->
                AppCompatResources.getDrawable(
                    context,
                    if (!category.selected) R.drawable.ic_catering_colour else R.drawable.ic_catering_white
                )!!
            Constants.Category.LEISURE ->
                AppCompatResources.getDrawable(
                    context,
                    if (!category.selected) R.drawable.ic_leisure_colour else R.drawable.ic_leisure_white
                )!!
            Constants.Category.SHOPPING ->
                AppCompatResources.getDrawable(
                    context,
                    if (!category.selected) R.drawable.ic_cart_colour else R.drawable.ic_cart_white
                )!!
            else ->
                AppCompatResources.getDrawable(
                    context,
                    if (!category.selected) R.drawable.ic_payment_regulated_parking_colour else R.drawable.ic_payment_regulated_parking_white
                )!!
        }

    private fun getCategoryColor(category: CategoryInfo) =
        when (category.name) {
            Constants.Category.BEAUTY ->
                ContextCompat.getColor(context, R.color.category_beauty)
            Constants.Category.FOOD ->
                ContextCompat.getColor(context, R.color.category_food)
            Constants.Category.LEISURE ->
                ContextCompat.getColor(context, R.color.category_leisure)
            Constants.Category.SHOPPING ->
                ContextCompat.getColor(context, R.color.category_shopping)
            else ->
                ContextCompat.getColor(context, R.color.category_other)
        }

    private fun getTextColorByCategory(category: CategoryInfo) =
        if (category.selected) ContextCompat.getColor(context, R.color.text_white)
        else getCategoryColor(category)

    private fun getBackgroundColorByCategory(category: CategoryInfo) =
        if (category.selected) getCategoryColor(category)
        else ContextCompat.getColor(context, R.color.text_white)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory = categoryList[position]

        holder.itemView.setOnClickListener {
            listener.onCategoryItemClick(currentCategory, position)
        }

        // Set category symbol
        Glide.with(context)
            .load(getCategorySymbol(currentCategory))
            .into(holder.ivCategorySymbol)

        // Set category name
        holder.tvCategoryName.text = currentCategory.name.asSentence()

        // Set text color by category
        holder.tvCategoryName.setTextColor(getTextColorByCategory(currentCategory))

        // Set background color
        holder.constraintLayout.setBackgroundColor(getBackgroundColorByCategory(currentCategory))
    }

    override fun getItemCount(): Int = categoryList.size

    inner class ViewHolder(binding: RecyclerCategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val constraintLayout = binding.constraintLayout
        val ivCategorySymbol = binding.ivCategorySymbol
        val tvCategoryName = binding.tvCategoryName
    }
}