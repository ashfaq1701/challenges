package com.omise.omisetest.screens.donations

import android.widget.EditText
import com.omise.omisetest.BR
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.common.viewModel.BaseViewModel
import javax.inject.Inject

class DonationViewModel(application: DonationApplication, val charity: Charity): BaseViewModel(application) {
    @Inject
    lateinit var donationRepository: DonationRepository

    private val _cardNumber = MutableLiveData<String>()
    val cardHolderName = MutableLiveData<String>()
    private val _cardExpiryMonth = MutableLiveData<Int>()
    private val _cardExpiryYear = MutableLiveData<Int>()
    val cardSecurityCode = MutableLiveData<String>()
    val amountTxt = MutableLiveData<String>()
    val amount: LiveData<Float> = Transformations.map(amountTxt) {
        it.toFloat()
    }

    private val _formSubmitted = MutableLiveData<Boolean>()
    val formValid = MutableLiveData<Boolean>()
    val submitEnabled = MutableLiveData<Boolean>()

    init {
        getViewModelComponent().inject(this)
    }

    fun submitForm() {
        _formSubmitted.value = true
    }

    fun setCardExpiryMonth(cardExpiryMonth: Int) {
        _cardExpiryMonth.value = cardExpiryMonth
    }

    fun setCardExpiryYear(cardExpiryYear: Int) {
        _cardExpiryYear.value = cardExpiryYear
    }

    fun setCardNumber(cardNumber: String) {
        _cardNumber.value = cardNumber
    }
}