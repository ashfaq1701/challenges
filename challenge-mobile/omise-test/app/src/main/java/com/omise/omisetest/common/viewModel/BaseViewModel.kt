package com.omise.omisetest.common.viewModel

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.AndroidViewModel
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.dependencyinjection.viewModel.ViewModelComponent
import com.omise.omisetest.common.dependencyinjection.viewModel.ViewModelModule

open class BaseViewModel(application: DonationApplication): AndroidViewModel(application) {
    private lateinit var mViewModelComponent: ViewModelComponent

    fun getViewModelComponent(): ViewModelComponent {
        if (::mViewModelComponent.isInitialized) {
            return mViewModelComponent
        }
        mViewModelComponent = getApplication<DonationApplication>()
            .getApplicationComponent()
            .newViewModelComponent(ViewModelModule())

        return mViewModelComponent
    }
}