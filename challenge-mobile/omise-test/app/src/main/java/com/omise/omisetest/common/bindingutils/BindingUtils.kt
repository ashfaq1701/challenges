package com.omise.omisetest.common.bindingutils

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
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

@BindingAdapter("editTextActiveControl")
fun setEditTextActiveControl(editText: EditText, isActive: LiveData<Boolean>) {
    val context = editText.context
    if (isActive.value == true) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
    } else {
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
        editText.setBackgroundColor(context.getColor(R.color.editTextDisabledBackground))
    }
}

@BindingAdapter("buttonActiveControl")
fun setButtonActiveControl(button: Button, isActive: LiveData<Boolean>) {
    val context = button.context
    if (isActive.value == true) {
        button.isEnabled = true
        button.setBackgroundColor(context.getColor(R.color.colorAccent))
    } else {
        button.isEnabled = false
        button.setBackgroundColor(context.getColor(R.color.colorDisabled))
    }
}