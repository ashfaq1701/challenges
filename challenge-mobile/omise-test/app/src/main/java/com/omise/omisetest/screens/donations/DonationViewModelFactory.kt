package com.omise.omisetest.screens.donations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.models.Charity

class DonationViewModelFactory(private val application: DonationApplication, private val donationRepository: DonationRepository, private val selectedCharity: Charity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DonationViewModel::class.java)) {
            return DonationViewModel(application, donationRepository, selectedCharity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}