package com.omise.omisetest.screens.charities

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
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.databinding.CharitiesScreenBinding
import javax.inject.Inject

class CharitiesScreen : BaseFragment() {
    @Inject
    lateinit var charitiesRepository:  CharitiesRepository

    private val viewModel: CharitiesViewModel by lazy {
        activity?.let {
            val application = it.application as DonationApplication
            val factory = CharitiesViewModelFactory(application, charitiesRepository)
            return@lazy ViewModelProvider(this, factory).get(CharitiesViewModel::class.java)
        }
        throw IllegalStateException("Activity not defined")
    }

    private lateinit var dataBinding: CharitiesScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPresentationComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Charities"
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.charities_screen, container, false)
        dataBinding.charitiesScreenViewModel = viewModel

        val adapter = CharitiesAdapter(CharityListener { charityId ->
            viewModel.onCharityClicked(charityId)
        })
        dataBinding.charitiesList.adapter = adapter

        viewModel.charities.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                if (it.isEmpty()) {
                    dataBinding.noItemAvailable.visibility = View.VISIBLE
                    dataBinding.charitiesList.visibility = View.GONE
                } else {
                    dataBinding.noItemAvailable.visibility = View.GONE
                    dataBinding.charitiesList.visibility = View.VISIBLE
                }
            }
        })

        viewModel.navigateToConnectionError.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(CharitiesScreenDirections.actionCharitiesScreenToConnectionErrorScreen())
                viewModel.navigateToConnectionError()
            }
        })

        viewModel.navigateToServerError.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(CharitiesScreenDirections.actionCharitiesScreenToServerErrorScreen())
                viewModel.resetNavigateToServerError()
            }
        })

        viewModel.navigateToDonation.observe(viewLifecycleOwner, Observer {
            if (it && viewModel.selectedCharity.value != null) {
                this.findNavController().navigate(CharitiesScreenDirections.actionCharitiesScreenToDonationScreen(viewModel.selectedCharity.value!!))
                viewModel.resetNavigateToDonation()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                dataBinding.progressBar.visibility = View.VISIBLE
            } else {
                dataBinding.progressBar.visibility = View.GONE
            }
        })

        dataBinding.lifecycleOwner = this

        return dataBinding.root
    }
}