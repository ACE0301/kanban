package com.ace.homework2.view.ui.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ace.homework2.model.cards.data.Card
import com.bumptech.glide.Glide
import com.woxthebox.draglistview.DragItemAdapter
import kotlinx.android.synthetic.main.column_item.view.*


class CardsAdapter(
    cards: ArrayList<Pair<Long, Card>>,
    private val layoutId: Int,
    private val grabHandleId: Int
) : DragItemAdapter<Pair<Long, Card>, CardsAdapter.ViewHolder>() {

    var onItemClickListener: ((Long) -> Unit) = {}

    init {
        itemList = cards
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false),
        onItemClickListener,
        grabHandleId
    )

    override fun getItemViewType(position: Int) = position

    override fun getUniqueItemId(position: Int) = itemList[position].first

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.bind(itemList[position])
    }

    class ViewHolder(
        itemView: View,
        private val listener: (Long) -> Unit,
        grabHandleId: Int
    ) : DragItemAdapter.ViewHolder(itemView, grabHandleId, true) {

        fun bind(item: Pair<Long, Card>) {
            itemView.tvCardName.text = item.second.name
            if (item.second.attachments.isNotEmpty()) {
                val preview = item.second.attachments.filter {
                    it.previews.isNotEmpty()
                }
                Glide
                    .with(itemView)
                    .load(preview[preview.size - 1].previews[4].url)
                    .override(600, 400)
                    .into(itemView.ivCardPreview)
                itemView.ivCardIconClip.visibility = View.VISIBLE
                itemView.tvAttachmentQuantity.visibility = View.VISIBLE
                itemView.tvAttachmentQuantity.text = item.second.attachments.size.toString()
            } else {
                Glide
                    .with(itemView).clear(itemView.ivCardPreview)
                itemView.ivCardIconClip.visibility = View.GONE
                itemView.tvAttachmentQuantity.visibility = View.GONE
            }
            if (item.second.desc.isNotEmpty()) {
                itemView.ivCardHasDescriptionIcon.visibility = View.VISIBLE
            } else itemView.ivCardHasDescriptionIcon.visibility = View.GONE
            if (item.second.idMembers.isNotEmpty()) {
                itemView.ivCardHasMembersIcon.visibility = View.VISIBLE
            }
            itemView.setOnClickListener {
                listener(item.first)
            }
        }
    }
}