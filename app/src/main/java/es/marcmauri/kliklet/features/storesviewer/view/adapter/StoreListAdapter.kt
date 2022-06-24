package es.marcmauri.kliklet.features.storesviewer.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.databinding.RecyclerStoreItemBinding
import es.marcmauri.kliklet.features.storesviewer.view.fragment.placeholder.PlaceholderContent.PlaceholderItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class StoreListAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<StoreListAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            RecyclerStoreItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        // todo: determine store symbol
        when (System.nanoTime() % 3) {
            0L -> holder.constraintLayoutStoreItemHeader.background =
                AppCompatResources.getDrawable(context, android.R.color.holo_orange_light)
            1L -> holder.constraintLayoutStoreItemHeader.background =
                AppCompatResources.getDrawable(context, android.R.color.holo_green_light)
            2L -> holder.constraintLayoutStoreItemHeader.background =
                AppCompatResources.getDrawable(context, android.R.color.holo_blue_light)
            else -> holder.constraintLayoutStoreItemHeader.background =
                AppCompatResources.getDrawable(context, android.R.color.background_dark)
        }

        Glide.with(context)
            .load(AppCompatResources.getDrawable(context, R.drawable.ic_ees_white))
            .into(holder.ivStoreSymbol)

        Glide.with(context)
            .load(AppCompatResources.getDrawable(context, R.drawable.ic_arrow_right_white))
            .into(holder.ivArrowRight)

        // todo: get store image from the Store object
        Glide.with(context)
            .load(AppCompatResources.getDrawable(context, R.drawable.ic_only_image))
            .into(holder.ivStoreImage)

        // todo: determine store distance
        holder.tvStoreDistance.text = "255m."

        // Todo: get data from Store object
        holder.tvStoreName.text = "Repsol"
        holder.tvStoreDescription.text = "Disfruta de 25 cent. de descuento solo hoy!"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: RecyclerStoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val constraintLayoutStoreItemHeader = binding.constraintLayoutStoreItemHeader
        val ivStoreSymbol = binding.ivStoreSymbol
        val tvStoreDistance = binding.tvStoreDistance
        val ivStoreImage = binding.ivStoreImage
        val tvStoreName = binding.tvStoreName
        val tvStoreDescription = binding.tvStoreDescription
        val ivArrowRight = binding.ivArrowRight


        override fun toString(): String {
            return "ViewHolder(ivStoreSymbol=$ivStoreSymbol, tvStoreDistance=$tvStoreDistance, ivStoreImage=$ivStoreImage, tvStoreName=$tvStoreName, tvStoreDescription=$tvStoreDescription)"
        }
    }
}