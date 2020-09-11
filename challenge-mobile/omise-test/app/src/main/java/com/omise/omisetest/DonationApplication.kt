package com.omise.omisetest

import android.app.Application
import com.omise.omisetest.common.dependencyinjection.application.ApplicationComponent
import com.omise.omisetest.common.dependencyinjection.application.ApplicationModule
import com.omise.omisetest.common.dependencyinjection.application.DaggerApplicationComponent

import timber.log.Timber

class DonationApplication: Application() {
    private lateinit var mApplicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule(this)
            )
            .build()
        mApplicationComponent.inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent = mApplicationComponent
}