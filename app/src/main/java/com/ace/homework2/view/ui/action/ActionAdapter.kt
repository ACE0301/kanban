package com.ace.homework2.view.ui.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.actions.data.ActionsPresModel
import com.ace.homework2.utils.DateUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_action.view.*

class ActionAdapter : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(
        this,
        ActionDiffCallback()
    )

    fun setData(data: List<ActionsPresModel>) {
        asyncListDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_action,
                parent,
                false
            )
        )

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(asyncListDiffer.currentList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(action: ActionsPresModel) {
            itemView.tvActionInfo.text = action.actionInfo
            if (action.memberCreator.avatar.isNullOrEmpty()) {
                itemView.civHistoryAvatar.setInitials(action.memberCreator.initials)
            } else {
                Glide.with(itemView)
                    .load(
                        action.memberCreator.avatar
                    )
                    .into(itemView.civHistoryAvatar)
            }
            Glide.with(itemView)
                .load(
                    action.data.attachment?.previewUrl
                )
                .into(itemView.ivPreview)
            itemView.tvTimeUpdated?.text = DateUtil(itemView.context).humanizeDiff(action.date)
        }
    }
}