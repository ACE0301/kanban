package com.ace.homework2.view.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.members.Member
import com.ace.homework2.view.ui.cards.MemberDiffCallback
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_card_member.view.*

class CardMembersAdapter : RecyclerView.Adapter<CardMembersAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(
        this,
        MemberDiffCallback()
    )

    fun setData(data: List<Member>) {
        asyncListDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_card_member,
                parent,
                false
            )
        )

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(asyncListDiffer.currentList[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: Member) {
            if (item.avatar == null) {
                itemView.civAvatar.setInitials(item.initials).let {

                }
            } else {
                Glide.with(itemView)
                    .load(
                        item.avatar
                    )
                    .into(itemView.civAvatar)
            }
        }
    }
}