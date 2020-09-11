package com.omise.omisetest.common.dependencyinjection.application

import com.omise.omisetest.DonationApplication
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(mApplication: DonationApplication)

}