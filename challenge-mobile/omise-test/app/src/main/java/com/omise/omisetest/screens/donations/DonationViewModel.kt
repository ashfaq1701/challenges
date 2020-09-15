package com.omise.omisetest.screens.donations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.common.viewModel.BaseViewModel
import timber.log.Timber
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

    val formFieldComplexLiveData = CreditCardComplexLiveData(_cardNumber, cardHolderName, _cardExpiryMonth, _cardExpiryYear, cardSecurityCode, amount)
    val formValid: LiveData<Boolean> = Transformations.switchMap(formFieldComplexLiveData) { liveData ->
        MutableLiveData<Boolean>((liveData.first != null) &&
                liveData.first.isNotEmpty() &&
                (liveData.second != null) &&
                liveData.second.isNotEmpty() &&
                (liveData.third != null) &&
                liveData.third != 0 &&
                (liveData.fourth != null) &&
                liveData.fourth != 0 &&
                (liveData.fifth != null) &&
                liveData.fifth.isNotEmpty() &&
                (liveData.sixth != null) &&
                liveData.sixth != 0F)
    }

    val formSubmitted = MutableLiveData<Boolean>(false)

    val formFieldsEnabled = Transformations.map(formSubmitted) { !it }

    val pairComplexLiveData = PairBooleanLiveData(formValid, formSubmitted)

    val submitEnabled: LiveData<Boolean> = Transformations.switchMap(pairComplexLiveData) { liveData ->
        MutableLiveData<Boolean>(liveData.first == true && liveData.second != true)
    }

    init {
        getViewModelComponent().inject(this)
    }

    fun submitForm() {
        Timber.d("FORM SUBMITTED")
        formSubmitted.value = true
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