package com.omise.omisetest.screens.charities

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omise.omisetest.R

@BindingAdapter("charityString")
fun TextView.setSleepQualityString(item: Charity?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val scheme = if (imgUrl.startsWith("https:")) {
            "https"
        } else {
            "http"
        }
        val imgUri = imgUrl.toUri().buildUpon().scheme(scheme).build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}