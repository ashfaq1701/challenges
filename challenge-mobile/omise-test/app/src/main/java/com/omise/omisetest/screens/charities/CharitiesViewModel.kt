package com.omise.omisetest.screens.charities

import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.viewModel.BaseViewModel
import javax.inject.Inject

class CharitiesViewModel(application: DonationApplication): BaseViewModel(application) {
    @Inject
    lateinit var charitiesRepository:  CharitiesRepository

    init {
        getViewModelComponent().inject(this)
    }
}