package com.omise.omisetest

import android.app.Application
import com.omise.omisetest.common.dependencyinjection.application.ApplicationComponent
import com.omise.omisetest.common.dependencyinjection.application.ApplicationModule
import com.omise.omisetest.common.dependencyinjection.application.DaggerApplicationComponent
import com.omise.omisetest.common.network.DonationsApiService

import timber.log.Timber
import javax.inject.Inject

class DonationApplication: Application() {
    private lateinit var mApplicationComponent: ApplicationComponent

    @Inject
    lateinit var mDonationsApiService: DonationsApiService

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule()
            )
            .build()
        mApplicationComponent.inject(this)
    }

    fun getDonationsApiService() = mDonationsApiService
}