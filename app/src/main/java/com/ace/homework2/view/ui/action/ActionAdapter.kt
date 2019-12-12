package com.ace.homework2.view.ui.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.actions.Action
import com.ace.homework2.utils.DateUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_action.view.*

class ActionAdapter : RecyclerView.Adapter<ActionAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(
        this,
        ActionDiffCallback()
    )

    fun setData(data: List<Action>) {
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
        fun bindData(action: Action) {
            if (action.memberCreator.avatar.isNullOrEmpty()) {
                itemView.civHistoryAvatar.setInitials(action.memberCreator.initials)
            } else {
                Glide.with(itemView)
                    .load(
                        action.memberCreator.avatar
                    )
                    .into(itemView.civHistoryAvatar)
            }
            when (action.type) {
                "addMemberToCard" -> {
                    itemView.tvActionInfo?.text = itemView.context.getString(
                        R.string.add_member_to_card_text,
                        action.memberCreator.fullName,
                        action.member.fullName
                    )
                }
                "createCard" -> itemView.tvActionInfo?.text =
                    itemView.context.getString(
                        R.string.create_card_text,
                        action.memberCreator.fullName,
                        action.data.list.name
                    )
                "addAttachmentToCard" -> {
                    itemView.tvActionInfo?.text = itemView.context.getString(
                        R.string.add_attachment_to_card_text,
                        action.memberCreator.fullName,
                        action.data.attachment.name
                    )
                    Glide.with(itemView)
                        .load(
                            action.data.attachment.previewUrl
                        )
                        .into(itemView.ivPreview)
                }
                "updateCard" -> {
                    itemView.tvActionInfo?.text =
                        when (action.data.old.desc) {
                            "" -> itemView.context.getString(
                                R.string.update_card_add_description_text,
                                action.memberCreator.fullName,
                                action.data.card.desc
                            )
                            else -> itemView.context.getString(
                                R.string.update_card_change_description_text,
                                action.memberCreator.fullName,
                                action.data.old.desc,
                                action.data.card.desc
                            )
                        }
                }
                else -> itemView.tvActionInfo?.text = ""
            }

            itemView.tvTimeUpdated?.text = DateUtil(itemView.context).humanizeDiff(action.date)
        }
    }
}