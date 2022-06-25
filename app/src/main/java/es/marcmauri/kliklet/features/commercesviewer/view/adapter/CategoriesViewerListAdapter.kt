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
import es.marcmauri.kliklet.databinding.RecyclerCategoryListItemBinding
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCategoriesViewerListListener
import es.marcmauri.kliklet.utils.Constants
import es.marcmauri.kliklet.utils.asSentence


class CategoriesViewerListAdapter(
    private val categoryList: List<String>,
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

    private fun getTextColorByCategory(category: String): Int =
        when (category) {
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

    private fun getCategorySymbol(category: String): Drawable =
        when (category) {
            Constants.Category.BEAUTY ->
                AppCompatResources.getDrawable(context, R.drawable.ic_car_wash_colour)!!
            Constants.Category.FOOD ->
                AppCompatResources.getDrawable(context, R.drawable.ic_catering_colour)!!
            Constants.Category.LEISURE ->
                AppCompatResources.getDrawable(context, R.drawable.ic_leisure_colour)!!
            Constants.Category.SHOPPING ->
                AppCompatResources.getDrawable(context, R.drawable.ic_cart_colour)!!
            else ->
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_payment_regulated_parking_colour
                )!!
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory = categoryList[position]

        holder.itemView.setOnClickListener {
            listener.onCategoryItemClick(currentCategory, position)
        }

        // Set category name
        holder.tvCategoryName.text = currentCategory.asSentence()

        // Set text color by category
        holder.tvCategoryName.setTextColor(getTextColorByCategory(currentCategory))

        // Set category symbol
        Glide.with(context)
            .load(getCategorySymbol(currentCategory))
            .into(holder.ivCategorySymbol)
    }

    override fun getItemCount(): Int = categoryList.size

    inner class ViewHolder(binding: RecyclerCategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivCategorySymbol = binding.ivCategorySymbol
        val tvCategoryName = binding.tvCategoryName
    }
}