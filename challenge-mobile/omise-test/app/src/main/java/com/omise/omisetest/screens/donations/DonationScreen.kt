package com.omise.omisetest.screens.donations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.R
import com.omise.omisetest.common.fragment.BaseFragment
import com.omise.omisetest.common.utils.DecimalDigitsInputFilter
import com.omise.omisetest.databinding.CharitiesScreenBinding
import com.omise.omisetest.databinding.DonationScreenBinding
import com.omise.omisetest.screens.charities.CharitiesViewModel
import com.omise.omisetest.screens.charities.CharitiesViewModelFactory
import javax.inject.Inject

class DonationScreen : BaseFragment() {
    private val viewModel: DonationViewModel by lazy {
        activity?.let {
            val args = DonationScreenArgs.fromBundle(requireArguments())
            val application = it.application as DonationApplication
            val factory = DonationViewModelFactory(application, args.selectedCharity)
            return@lazy ViewModelProvider(this, factory).get(DonationViewModel::class.java)
        }
        throw IllegalStateException("Activity not defined")
    }

    private lateinit var dataBinding: DonationScreenBinding

    /**
     * Definitely an overkill and stupidish. I just wanted to show how a presentation dependency can be injected
     */
    @Inject
    lateinit var decimalDigitsInputFilter: DecimalDigitsInputFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresentationComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Donate"
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.donation_screen, container, false)
        dataBinding.donationScreenViewModel = viewModel

        dataBinding.amount.filters = arrayOf(decimalDigitsInputFilter)

        dataBinding.setLifecycleOwner(this)
        return dataBinding.root
    }
}