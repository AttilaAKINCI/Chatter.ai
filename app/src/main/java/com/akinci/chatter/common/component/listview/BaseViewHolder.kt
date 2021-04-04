package com.akinci.chatter.common.component.listview

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in Model>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(data: Model)
}