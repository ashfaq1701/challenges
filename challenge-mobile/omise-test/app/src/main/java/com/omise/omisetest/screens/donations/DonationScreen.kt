package com.omise.omisetest.screens.donations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.R
import com.omise.omisetest.common.fragment.BaseFragment
import com.omise.omisetest.common.utils.DecimalDigitsInputFilter
import com.omise.omisetest.databinding.DonationScreenBinding
import javax.inject.Inject

class DonationScreen : BaseFragment() {
    @Inject
    lateinit var donationRepository: DonationRepository

    private val viewModel: DonationViewModel by lazy {
        activity?.let {
            val args = DonationScreenArgs.fromBundle(requireArguments())
            val application = it.application as DonationApplication
            val factory = DonationViewModelFactory(application, donationRepository, args.selectedCharity)
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

        /**
         * I really didn't want to do this, but omise SDK doesn't have binding adapters :(
         */
        dataBinding.cardExpiryDate.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.setCardExpiryMonth(dataBinding.cardExpiryDate.expiryMonth)
                viewModel.setCardExpiryYear(dataBinding.cardExpiryDate.expiryYear)
            }
        }

        /**
         * Same, would be better if it could be avoided :(
         */
        dataBinding.cardNumber.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.setCardNumber(dataBinding.cardNumber.cardNumber)
            }
        }

        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (!it) {
                dataBinding.progressBarDonationPage.visibility = View.GONE
            } else {
                dataBinding.progressBarDonationPage.visibility = View.VISIBLE
            }
        })

        viewModel.navigateToPaymentSuccessfulScreen.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(DonationScreenDirections.actionDonationScreenToDonationSuccessfulScreen())
                viewModel.resetNavigateToPaymentSuccessfulScreen()
            }
        })

        viewModel.navigateToConnectionErrorScreen.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(DonationScreenDirections.actionDonationScreenToConnectionErrorScreen())
                viewModel.resetNavigateToConnectionErrorScreen()
            }
        })

        viewModel.navigateToServerErrorScreen.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(DonationScreenDirections.actionDonationScreenToServerErrorScreen())
                viewModel.resetNavigateToServerErrorScreen()
            }
        })

        dataBinding.lifecycleOwner = this
        return dataBinding.root
    }
}