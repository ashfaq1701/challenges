package com.omise.omisetest.common.dependencyinjection.viewModel

import com.omise.omisetest.common.network.DonationsApiService
import com.omise.omisetest.screens.charities.CharitiesRepository
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun getCharitiesRepository(donationsApiService: DonationsApiService) = CharitiesRepository(donationsApiService)
}