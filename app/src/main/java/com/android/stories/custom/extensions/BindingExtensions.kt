package com.android.stories.custom.extensions

import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


@BindingAdapter("imageUrl", "rounded")
fun setCurrentIndex(
    imageView: AppCompatImageView,
    url: String?,
    rounded: Boolean = false
) {
    val glideImage = Glide.with(imageView.context).load(url)

    if (rounded)
        glideImage.circleCrop()

    glideImage.into(imageView)
}

@BindingAdapter("adapter")
fun recyclerViewAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    recyclerView.adapter = adapter
}

@BindingAdapter("divider")
fun recyclerViewDivider(recyclerView: RecyclerView, @DrawableRes divider: Int) {
    val dividerItemDecoration = DividerItemDecoration(
        recyclerView.context,
        (recyclerView.layoutManager as LinearLayoutManager).orientation
    )

    ContextCompat.getDrawable(recyclerView.context, divider)?.let {
        dividerItemDecoration.setDrawable(it)
    }

    recyclerView.addItemDecoration(dividerItemDecoration)
}