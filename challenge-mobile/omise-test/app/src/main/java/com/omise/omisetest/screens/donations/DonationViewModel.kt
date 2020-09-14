package com.omise.omisetest.screens.donations

import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.common.viewModel.BaseViewModel
import com.omise.omisetest.screens.charities.CharitiesRepository
import javax.inject.Inject

class DonationViewModel(application: DonationApplication, val charity: Charity): BaseViewModel(application) {
    @Inject
    lateinit var donationRepository: DonationRepository

    init {
        getViewModelComponent().inject(this)
    }
}