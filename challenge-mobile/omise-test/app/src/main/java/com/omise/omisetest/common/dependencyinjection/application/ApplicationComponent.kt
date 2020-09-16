package com.omise.omisetest.common.dependencyinjection.application

import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.dependencyinjection.presentation.PresentationComponent
import com.omise.omisetest.common.dependencyinjection.presentation.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(mApplication: DonationApplication)

    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
}