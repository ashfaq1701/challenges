package com.omise.omisetest.screens.charities

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.R
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.databinding.CharitiesScreenBinding
import timber.log.Timber

class CharitiesScreen : Fragment() {
    private val viewModel: CharitiesViewModel by lazy {
        activity?.let {
            val application = it.application as DonationApplication
            val factory = CharitiesViewModelFactory(application)
            return@lazy ViewModelProvider(this, factory).get(CharitiesViewModel::class.java)
        }
        throw IllegalStateException("Activity not defined")
    }

    private lateinit var dataBinding: CharitiesScreenBinding

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
            }
        })

        viewModel.selectedCharity.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(CharitiesScreenDirections.actionCharitiesScreenToDonationScreen(it))
                viewModel.doneNavigating()
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            if (status == ApiStatus.LOADING) {
                dataBinding.progressBar.visibility = View.VISIBLE
            } else {
                dataBinding.progressBar.visibility = View.GONE
                if (status == ApiStatus.NoInternet) {
                    this.findNavController().navigate(CharitiesScreenDirections.actionCharitiesScreenToConnectionErrorScreen())
                    viewModel.doneNavigating()
                } else if (status == ApiStatus.ERROR) {
                    this.findNavController().navigate(CharitiesScreenDirections.actionCharitiesScreenToServerErrorScreen())
                    viewModel.doneNavigating()
                }
            }
        })

        dataBinding.setLifecycleOwner(this)

        return dataBinding.root
    }
}