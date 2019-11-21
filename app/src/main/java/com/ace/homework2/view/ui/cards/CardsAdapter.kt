package com.ace.homework2.view.ui.cards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.Pair
import com.ace.homework2.R
import com.woxthebox.draglistview.DragItemAdapter


class CardsAdapter(
    private val list: ArrayList<Pair<Long, String>>,
    private val layoutId: Int,
    private val grabHandleId: Int
) : DragItemAdapter<Pair<Long, String>, CardsAdapter.ViewHolder>() {

    var onItemClickListener: ((Long) -> Unit) = {}

    init {
        itemList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    )

    override fun getUniqueItemId(position: Int): Long {
        return itemList[position].first!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val text = mItemList[position].second
        holder.cardName.text = text
    }

    inner class ViewHolder(itemView: View) :
        DragItemAdapter.ViewHolder(itemView, grabHandleId, true) {

        val cardName: TextView = itemView.findViewById(R.id.cardName)

        override fun onItemClicked(view: View?) {
            itemList[position].first?.let { onItemClickListener.invoke(it) }
        }
    }

}