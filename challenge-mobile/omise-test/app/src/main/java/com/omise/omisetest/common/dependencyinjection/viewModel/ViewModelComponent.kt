package com.omise.omisetest.common.dependencyinjection.viewModel

import com.omise.omisetest.screens.charities.CharitiesViewModel
import com.omise.omisetest.screens.donations.DonationViewModel
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    fun inject(charitiesViewModel: CharitiesViewModel)

    fun inject(donationViewModel: DonationViewModel)
}