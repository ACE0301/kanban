package com.ace.homework2.view.ui.members

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.members.data.Member
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_board_member.view.*

class BoardMembersAdapter : RecyclerView.Adapter<BoardMembersAdapter.ViewHolder>() {

    var onItemClickListener: ((View, Member) -> Unit) = { view, member -> Unit }

    var data: Pair<List<Member>, List<Member>> = Pair(emptyList(), emptyList())
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
            ), onItemClickListener
        )

    override fun getItemCount() = data.first.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data.first[position], data.second)
    }

    class ViewHolder(itemView: View, private val listener: ((View, Member) -> Unit)) :
        RecyclerView.ViewHolder(itemView) {
        fun bindData(boardMember: Member, cardMembers: List<Member>) {
            if (boardMember.avatar == null) {
                itemView.ivAvatar.setInitials(boardMember.initials)
            } else {
                Glide.with(itemView)
                    .load(
                        boardMember.avatar
                    )
                    .into(itemView.ivAvatar)
            }
            itemView.tvMemberName.text = boardMember.fullName
            itemView.tvNickName.text = boardMember.username
            cardMembers.forEach {
                if (it.id == boardMember.id) {
                    itemView.ivCheckCardMember.visibility = View.VISIBLE
                }
            }

            itemView.setOnClickListener {
                listener(itemView, boardMember)
            }
        }
    }
}