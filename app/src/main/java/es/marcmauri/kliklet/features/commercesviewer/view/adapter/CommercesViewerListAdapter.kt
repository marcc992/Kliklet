package es.marcmauri.kliklet.features.commercesviewer.view.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.prefs
import es.marcmauri.kliklet.common.Constants
import es.marcmauri.kliklet.common.distanceToInKm
import es.marcmauri.kliklet.databinding.RecyclerCommerceListItemBinding
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerCommercesViewerListListener


class CommercesViewerListAdapter(
    private val commerceList: List<Commerce>,
    private val listener: RecyclerCommercesViewerListListener
) : RecyclerView.Adapter<CommercesViewerListAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            RecyclerCommerceListItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    private fun getHeaderColorByCategory(category: String): Drawable =
        when (category) {
            Constants.Category.BEAUTY ->
                AppCompatResources.getDrawable(context, R.color.category_beauty)!!
            Constants.Category.FOOD ->
                AppCompatResources.getDrawable(context, R.color.category_food)!!
            Constants.Category.LEISURE ->
                AppCompatResources.getDrawable(context, R.color.category_leisure)!!
            Constants.Category.SHOPPING ->
                AppCompatResources.getDrawable(context, R.color.category_shopping)!!
            else ->
                AppCompatResources.getDrawable(context, R.color.category_other)!!
        }

    private fun getCategorySymbol(category: String): Drawable =
        when (category) {
            Constants.Category.BEAUTY ->
                AppCompatResources.getDrawable(context, R.drawable.ic_car_wash_white)!!
            Constants.Category.FOOD ->
                AppCompatResources.getDrawable(context, R.drawable.ic_catering_white)!!
            Constants.Category.LEISURE ->
                AppCompatResources.getDrawable(context, R.drawable.ic_leisure_white)!!
            Constants.Category.SHOPPING ->
                AppCompatResources.getDrawable(context, R.drawable.ic_cart_white)!!
            else ->
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_payment_regulated_parking_white
                )!!
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCommerce = commerceList[position]

        // Set on Commerce holder click listener
        holder.itemView.setOnClickListener {
            listener.onCommerceItemClick(currentCommerce, position)
        }

        // Set header color by category
        holder.constraintLayoutCommerceItemHeader.background =
            getHeaderColorByCategory(currentCommerce.category ?: Constants.Category.OTHER)

        // Set category image
        Glide.with(context)
            .load(getCategorySymbol(currentCommerce.category ?: Constants.Category.OTHER))
            .into(holder.ivCommerceSymbol)

        // Set commerce thumbnail
        Glide.with(context)
            .load(currentCommerce.logo?.thumbnails?.small)
            .placeholder(AppCompatResources.getDrawable(context, R.drawable.ic_placeholder))
            .into(holder.ivCommerceImage)

        // TODO: Encontrar la manera de que el Recycler no lo borre sin necesitar este codigo:
        Glide.with(context)
            .load(AppCompatResources.getDrawable(context, R.drawable.ic_arrow_right_white))
            .into(holder.ivArrowRight)

        // todo: determine Commerce distance
        var distance = "- km."
        if (prefs.isLastLocationReady() && currentCommerce.latitude != null && currentCommerce.longitude != null) {
            val origin = prefs.lastLocation
            val destination = LatLng(currentCommerce.latitude, currentCommerce.longitude)
            distance = "${String.format("%.2f", origin.distanceToInKm(destination))} km."
        }
        holder.tvCommerceDistance.text = distance

        // Set commerce title and description
        holder.tvCommerceName.text = currentCommerce.name
        holder.tvCommerceDescription.text = currentCommerce.description
    }

    override fun getItemCount(): Int = commerceList.size

    inner class ViewHolder(binding: RecyclerCommerceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val constraintLayoutCommerceItemHeader = binding.constraintLayoutCommerceItemHeader
        val ivCommerceSymbol = binding.ivCommerceSymbol
        val tvCommerceDistance = binding.tvCommerceDistance
        val ivCommerceImage = binding.ivCommerceImage
        val tvCommerceName = binding.tvCommerceName
        val tvCommerceDescription = binding.tvCommerceDescription
        val ivArrowRight = binding.ivArrowRight
    }
}