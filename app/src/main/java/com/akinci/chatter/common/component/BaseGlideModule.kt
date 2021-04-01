package com.akinci.chatter.common.component

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

@GlideModule
class BaseGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.apply { RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(ObjectKey(System.currentTimeMillis().toShort())) }
    }
}

// Glide image loading with extension function
fun ImageView.setGlideImageCentered(imageUrl: String){
    GlideApp.with(context)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}