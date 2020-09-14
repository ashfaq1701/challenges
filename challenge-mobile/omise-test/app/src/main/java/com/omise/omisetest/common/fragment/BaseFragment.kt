package com.omise.omisetest.common.fragment

import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.dependencyinjection.application.ApplicationComponent
import com.omise.omisetest.common.dependencyinjection.presentation.PresentationComponent
import com.omise.omisetest.common.dependencyinjection.presentation.PresentationModule

abstract class BaseFragment: Fragment() {

    private var mIsInjectorUsed = false
    @UiThread
    fun getPresentationComponent(): PresentationComponent {
        if (mIsInjectorUsed) {
            throw RuntimeException("there is no need to use injector more than once")
        }
        mIsInjectorUsed = true
        return getApplicationComponent()
            .newPresentationComponent(PresentationModule())
    }

    private fun getApplicationComponent(): ApplicationComponent {
        return (requireActivity().getApplication() as DonationApplication).getApplicationComponent()
    }
}