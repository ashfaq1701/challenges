package com.omise.omisetest.common.dependencyinjection.presentation

import com.omise.omisetest.screens.charities.CharitiesScreen
import com.omise.omisetest.screens.charities.CharitiesViewModel
import com.omise.omisetest.screens.donations.DonationScreen
import com.omise.omisetest.screens.donations.DonationViewModel
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {
    fun inject(donationScreen: DonationScreen)

    fun inject(charitiesScreen: CharitiesScreen)
}