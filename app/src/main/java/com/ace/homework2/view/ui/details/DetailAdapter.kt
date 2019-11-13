package com.ace.homework2.view.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.Pair
import com.ace.homework2.R
import com.woxthebox.draglistview.DragItemAdapter


class DetailAdapter(
    list: ArrayList<Pair<Long, String>>,
    private val layoutId: Int,
    private val grabHandleId: Int,
    private val dragOnLongPress: Boolean
) : DragItemAdapter<Pair<Long, String>, DetailAdapter.ViewHolder>() {

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
        holder.mText.text = text
        holder.itemView.tag = mItemList[position]
    }

    inner class ViewHolder(itemView: View) : DragItemAdapter.ViewHolder(itemView, grabHandleId, dragOnLongPress) {

        val mText: TextView = itemView.findViewById(R.id.text)

        override fun onItemClicked(view: View?) {
            Toast.makeText(view?.context, "Item clicked", Toast.LENGTH_SHORT).show()
        }

        override fun onItemLongClicked(view: View?): Boolean {
            Toast.makeText(view?.context, "Item long clicked", Toast.LENGTH_SHORT).show()
            return true
        }
    }

}