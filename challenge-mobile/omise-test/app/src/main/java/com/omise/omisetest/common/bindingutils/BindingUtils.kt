package com.omise.omisetest.common.bindingutils

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.MutableLiveData
import co.omise.android.ui.CreditCardEditText
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omise.omisetest.R
import com.omise.omisetest.common.models.Charity

@BindingAdapter("charityString")
fun TextView.setCharityString(item: Charity?) {
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
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .into(imgView)
    }
}

