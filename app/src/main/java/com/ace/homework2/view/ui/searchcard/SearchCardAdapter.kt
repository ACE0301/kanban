package com.ace.homework2.view.ui.searchcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.cards.Card
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.column_item.view.*

class SearchCardAdapter : RecyclerView.Adapter<SearchCardAdapter.ViewHolder>() {

    var onItemClickListener: ((String) -> Unit) = {}

    private val asyncListDiffer = AsyncListDiffer(
        this,
        SearchCardDiffCallback()
    )

    fun setData(data: List<Card>) {
        asyncListDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.column_item, parent, false),
        onItemClickListener
    )

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    class ViewHolder(itemView: View, private val listener: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(card: Card) {
            itemView.tvCardName.text = card.name
            if (card.attachments.isNotEmpty()) {
                if (card.attachments[card.attachments.size - 1].previews.isNotEmpty()) {
                    Glide.with(itemView)
                        .load(
                            card.attachments[card.attachments.size - 1].previews[1].url
                        )
                        .into(itemView.ivCardPreview)
                }
                itemView.ivCardIconClip.visibility = View.VISIBLE
                itemView.tvAttachmentQuantity.visibility = View.VISIBLE
                itemView.tvAttachmentQuantity.text = card.attachments.size.toString()
            } else {
                Glide.with(itemView).clear(itemView.ivCardPreview)
                itemView.ivCardIconClip.visibility = View.GONE
                itemView.tvAttachmentQuantity.visibility = View.GONE
            }
            if (card.desc.isNotEmpty()) {
                itemView.ivCardHasDescriptionIcon.visibility = View.VISIBLE
            } else {
                itemView.ivCardHasDescriptionIcon.visibility = View.GONE
            }
            if (card.idMembers.isNotEmpty()) {
                itemView.ivCardHasMembersIcon.visibility = View.VISIBLE
            } else {
                itemView.ivCardHasMembersIcon.visibility = View.GONE
            }
            itemView.setOnClickListener {
                listener.invoke(card.id)
            }
        }
    }
}