package com.omise.omisetest.common.network

import retrofit2.http.GET

interface DonationsApiService {
    @GET("charities")
    suspend fun getCharities(): List<Charity>
}