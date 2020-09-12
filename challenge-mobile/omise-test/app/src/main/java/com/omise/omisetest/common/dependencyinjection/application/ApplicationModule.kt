package com.omise.omisetest.common.dependencyinjection.application

import android.app.Application
import com.omise.omisetest.common.network.DonationsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule() {
    companion object {
        val BASE_URL = "http://localhost:8080/"
    }

    @Provides
    fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun getRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun donationsApiService(retrofit: Retrofit): DonationsApiService {
        return retrofit.create(DonationsApiService::class.java)
    }
}