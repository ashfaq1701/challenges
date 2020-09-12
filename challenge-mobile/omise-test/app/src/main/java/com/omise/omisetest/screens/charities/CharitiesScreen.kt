package com.omise.omisetest.screens.charities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.charities_screen, container, false)
        dataBinding.charitiesScreenViewModel = viewModel
        return inflater.inflate(R.layout.charities_screen, container, false)
    }
}