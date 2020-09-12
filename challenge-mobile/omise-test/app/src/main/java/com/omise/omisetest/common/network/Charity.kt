package com.omise.omisetest.common.network

import com.squareup.moshi.Json

data class Charity (
    val id: Int,
    val name: String,
    @Json(name = "logo_url")
    val logoUrl: String
)