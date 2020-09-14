package com.omise.omisetest.common.dependencyinjection.presentation

import com.omise.omisetest.common.network.DonationsApiService
import com.omise.omisetest.common.utils.DecimalDigitsInputFilter
import com.omise.omisetest.screens.charities.CharitiesRepository
import com.omise.omisetest.screens.donations.DonationRepository
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {
    /**
     * Definitely an overkill and stupidish. I just wanted to show how a presentation dependency can be injected
     */
    @Provides
    fun getDecimalDigitsInputFilter() = DecimalDigitsInputFilter(10, 2)

}