package com.omise.omisetest.common.dependencyinjection.application

import android.app.Application
import co.omise.android.api.Client
import com.omise.omisetest.common.network.DonationsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseUrl: String, private val omisePKey: String) {
    @Provides
    fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun getRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun donationsApiService(retrofit: Retrofit): DonationsApiService {
        return retrofit.create(DonationsApiService::class.java)
    }

    @Provides
    @Singleton
    fun getClient() = Client(omisePKey)
}