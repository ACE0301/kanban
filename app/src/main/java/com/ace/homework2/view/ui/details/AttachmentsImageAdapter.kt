package com.ace.homework2.view.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.ace.homework2.R
import com.ace.homework2.model.cards.Attachments
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_preview_image.view.*

class AttachmentsImageAdapter : RecyclerView.Adapter<AttachmentsImageAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(
        this,
        AttachmentsDiffCallback()
    )

    fun setData(data: List<Attachments>) {
        asyncListDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_preview_image,
                parent,
                false
            )
        )

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(asyncListDiffer.currentList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(attachments: Attachments) {
            if (attachments.previews.isNotEmpty()) {
                Glide
                    .with(itemView)
                    .load(attachments.previews[4].url)
                    .into(itemView.ivPreviewImage)
            }
        }
    }
}