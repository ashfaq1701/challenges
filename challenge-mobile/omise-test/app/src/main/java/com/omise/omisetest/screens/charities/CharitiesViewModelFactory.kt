package com.omise.omisetest.screens.charities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omise.omisetest.DonationApplication

class CharitiesViewModelFactory(private val application: DonationApplication): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharitiesViewModel::class.java)) {
            return CharitiesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}