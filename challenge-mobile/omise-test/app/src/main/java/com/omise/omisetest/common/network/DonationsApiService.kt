package com.omise.omisetest.common.network


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface DonationsApiService {
    @GET("charities")
    suspend fun getCharities(): List<Charity>

    @Headers("Content-Type: application/json")
    @POST("donations")
    suspend fun createDonation(@Body charge: Charge): String
}