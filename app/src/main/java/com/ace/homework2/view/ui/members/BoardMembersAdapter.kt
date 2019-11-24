package com.ace.homework2.view.ui.members

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.Member
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_board_member.view.*
import kotlinx.android.synthetic.main.item_member.view.*
import kotlinx.android.synthetic.main.item_member.view.ivNoImage
import kotlinx.android.synthetic.main.item_member.view.tvMemberName

class BoardMembersAdapter : RecyclerView.Adapter<BoardMembersAdapter.ViewHolder>() {

    var data: List<Member> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_board_member,
                parent,
                false
            )
        )


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: Member) {
            if (item.avatarHash == null) {
                itemView.ivNoImage.setText(item.initials)
            } else {
                Glide.with(itemView)
                    .load(
                        "https://trello-avatars.s3.amazonaws.com/${item.avatarHash}/170.png"
                    )
                    .into(itemView.ivNoImage)
            }
            itemView.tvMemberName.text = item.fullName
            itemView.tvNickName.text = item.username
        }
    }
}