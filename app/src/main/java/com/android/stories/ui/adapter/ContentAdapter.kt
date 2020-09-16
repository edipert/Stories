package com.android.stories.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.stories.custom.data.model.Content
import com.android.stories.databinding.ItemContentBinding

class ContentAdapter(
    private val itemClick: (Int) -> Unit
) : RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    private val contentList: ArrayList<Content> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContentBinding.inflate(inflater)
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(position, contentList[position])
    }

    override fun getItemCount() = contentList.size

    fun setItems(contentList: List<Content>) {
        this.contentList.clear()
        this.contentList.addAll(contentList)
        notifyDataSetChanged()
    }

    inner class ContentViewHolder(
        private val viewBinding: ItemContentBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, content: Content) {
            viewBinding.model = content
            viewBinding.position = position
            viewBinding.clickListener = object : ItemClickListener {
                override fun onClick(position: Int) {
                    itemClick(position)
                }
            }
            viewBinding.executePendingBindings()
        }
    }
}