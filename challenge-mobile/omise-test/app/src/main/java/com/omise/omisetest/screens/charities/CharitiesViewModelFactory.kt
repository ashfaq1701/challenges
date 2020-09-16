package com.omise.omisetest.screens.charities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omise.omisetest.DonationApplication

class CharitiesViewModelFactory(private val application: DonationApplication, private val charitiesRepository: CharitiesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharitiesViewModel::class.java)) {
            return CharitiesViewModel(application, charitiesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}