package es.marcmauri.kliklet.features.commercesviewer.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.marcmauri.kliklet.databinding.RecyclerButtonListItemBinding
import es.marcmauri.kliklet.databinding.RecyclerButtonListItemSelectedBinding
import es.marcmauri.kliklet.features.commercesviewer.model.entities.ButtonInfo
import es.marcmauri.kliklet.features.commercesviewer.view.listener.RecyclerButtonsViewerListListener


class ButtonsViewerListAdapter(
    private val buttonList: List<ButtonInfo>,
    private val listener: RecyclerButtonsViewerListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        when (viewType) {
            0 -> {
                return ViewHolderSelected(
                    RecyclerButtonListItemSelectedBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                return ViewHolder(
                    RecyclerButtonListItemBinding.inflate(
                        LayoutInflater.from(context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (buttonList[position].selected) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentButton = buttonList[position]

        holder.itemView.setOnClickListener {
            // todo: (I+D) cambiar layout para que parezca como seleccionado
            listener.onButtonItemClick(currentButton, position)
        }

        // Populate button info either Selected or Unselected
        when (holder.itemViewType) {
            0 -> {
                (holder as ViewHolderSelected).tvDescription.text = currentButton.description
                (holder as ViewHolderSelected).tvCount.text = currentButton.count.toString()
            }
            else -> {
                (holder as ViewHolder).tvDescription.text = currentButton.description
                (holder as ViewHolder).tvCount.text = currentButton.count.toString()
            }
        }
    }

    override fun getItemCount(): Int = buttonList.size


    inner class ViewHolder(binding: RecyclerButtonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvCount = binding.tvCount
        val tvDescription = binding.tvDescription
    }

    inner class ViewHolderSelected(binding: RecyclerButtonListItemSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvCount = binding.tvCount
        val tvDescription = binding.tvDescription
    }
}