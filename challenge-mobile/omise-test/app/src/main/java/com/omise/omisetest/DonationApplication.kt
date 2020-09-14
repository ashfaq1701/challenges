package com.omise.omisetest

import android.content.pm.PackageManager
import androidx.multidex.MultiDexApplication
import com.omise.omisetest.common.dependencyinjection.application.ApplicationComponent
import com.omise.omisetest.common.dependencyinjection.application.ApplicationModule
import com.omise.omisetest.common.dependencyinjection.application.DaggerApplicationComponent
import com.omise.omisetest.common.network.DonationsApiService

import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

class DonationApplication: MultiDexApplication() {
    private lateinit var mApplicationComponent: ApplicationComponent

    @Inject
    lateinit var mDonationsApiService: DonationsApiService

    override fun onCreate() {
        super.onCreate()

        val bundle = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
        val baseUrl = bundle.getString("api_base_path") ?: throw IllegalArgumentException("Api base url not set in manifest!!")
        val omisePKey = bundle.getString("omise_api_public_key") ?: throw IllegalArgumentException("Omise public key not set in manifest!!")
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(
                ApplicationModule(baseUrl, omisePKey)
            )
            .build()
        mApplicationComponent.inject(this)
    }

    fun getApplicationComponent() = mApplicationComponent

    fun getDonationsApiService() = mDonationsApiService
}