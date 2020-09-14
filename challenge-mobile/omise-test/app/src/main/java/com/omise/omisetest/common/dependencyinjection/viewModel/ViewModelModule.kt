package com.omise.omisetest.common.dependencyinjection.viewModel

import co.omise.android.api.Client
import com.omise.omisetest.common.network.DonationsApiService
import com.omise.omisetest.screens.charities.CharitiesRepository
import com.omise.omisetest.screens.donations.DonationRepository
import dagger.Module
import dagger.Provides
import java.text.NumberFormat

@Module
class ViewModelModule {
    @Provides
    fun getCharitiesRepository(donationsApiService: DonationsApiService) = CharitiesRepository(donationsApiService)

    @Provides
    fun getDonationRepository(donationsApiService: DonationsApiService, omiseClient: Client) = DonationRepository(donationsApiService, omiseClient)
}