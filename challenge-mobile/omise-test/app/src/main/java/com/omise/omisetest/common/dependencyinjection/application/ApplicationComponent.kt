package com.omise.omisetest.common.dependencyinjection.application

import com.omise.omisetest.DonationApplication
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(mApplication: DonationApplication)

}