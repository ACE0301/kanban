package com.ace.homework2.view.ui.action

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.extentions.humanizeDiff
import com.ace.homework2.extentions.toDate
import com.ace.homework2.model.actions.Action
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
                com.ace.homework2.R.layout.item_action,
                parent,
                false
            )
        )

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(asyncListDiffer.currentList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(action: Action) {
            if (action.memberCreator.avatarHash == null) {
                itemView.civHistoryAvatar.setInitials(action.memberCreator.initials)
            } else {
                Glide.with(itemView)
                    .load(
                        "https://trello-avatars.s3.amazonaws.com/${action.memberCreator.avatarHash}/170.png"
                    )
                    .into(itemView.civHistoryAvatar)
            }
            when (action.type) {
                "addMemberToCard" -> {
                    itemView.tvActionInfo?.text =
                        "${action.memberCreator.fullName} добавил(а) участника ${action.member.fullName} к этой карточке"

                }
                "createCard" -> itemView.tvActionInfo?.text =
                    "${action.memberCreator.fullName} добавил(а) эту карточку в список ${action.data.list.name}"

                "addAttachmentToCard" -> {
                    itemView.tvActionInfo?.text =
                        "${action.memberCreator.fullName} прикрепил(а) вложение ${action.data.attachment.name} к этой карточке "
                    Glide.with(itemView)
                        .load(
                            action.data.attachment.previewUrl
                        )
                        .into(itemView.ivPreview)
                }
                "updateCard" -> {
                    itemView.tvActionInfo?.text =
                        when (action.data.old.desc) {
                            "" -> "${action.memberCreator.fullName} добавил(а) описание карточки: ${action.data.card.desc}"
                            else -> "${action.memberCreator.fullName} изменил(а) описание карточки с ${action.data.old.desc} на ${action.data.card.desc}"
                        }
                }
                else -> itemView
            }
            itemView.tvTimeUpdated?.text = action.date.toDate()?.humanizeDiff()

        }
    }
}