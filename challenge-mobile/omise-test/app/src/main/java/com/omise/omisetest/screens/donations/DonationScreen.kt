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
import com.omise.omisetest.databinding.CharitiesScreenBinding
import com.omise.omisetest.databinding.DonationScreenBinding
import com.omise.omisetest.screens.charities.CharitiesViewModel
import com.omise.omisetest.screens.charities.CharitiesViewModelFactory

class DonationScreen : Fragment() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Donate"
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.donation_screen, container, false)
        dataBinding.donationScreenViewModel = viewModel

        dataBinding.setLifecycleOwner(this)
        return dataBinding.root
    }
}