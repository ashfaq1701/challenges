package com.omise.omisetest.common.dependencyinjection.presentation

import com.omise.omisetest.screens.charities.CharitiesScreen
import com.omise.omisetest.screens.donations.DonationScreen
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent {
    fun inject(donationScreen: DonationScreen)

    fun inject(charitiesScreen: CharitiesScreen)
}