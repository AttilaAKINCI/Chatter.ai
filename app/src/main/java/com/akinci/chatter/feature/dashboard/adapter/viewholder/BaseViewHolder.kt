package com.akinci.chatter.feature.dashboard.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in Model>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: Model)
}