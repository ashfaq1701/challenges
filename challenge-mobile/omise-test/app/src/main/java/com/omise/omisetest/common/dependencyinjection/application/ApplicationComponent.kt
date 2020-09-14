package com.omise.omisetest.common.dependencyinjection.application

import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.dependencyinjection.presentation.PresentationComponent
import com.omise.omisetest.common.dependencyinjection.presentation.PresentationModule
import com.omise.omisetest.common.dependencyinjection.viewModel.ViewModelComponent
import com.omise.omisetest.common.dependencyinjection.viewModel.ViewModelModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {
    fun inject(mApplication: DonationApplication)

    fun newViewModelComponent(viewModelModule: ViewModelModule): ViewModelComponent

    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
}