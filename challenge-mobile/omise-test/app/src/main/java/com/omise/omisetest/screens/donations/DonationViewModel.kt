package com.omise.omisetest.screens.donations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charge
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.common.models.CreditCard
import com.omise.omisetest.common.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
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
        if (it.isEmpty()) {
            0F
        } else {
            it.toFloat()
        }
    }
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

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
        formSubmitted.value = true
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            donationRepository.createToken(CreditCard(
                cardNumber = _cardNumber.value!!,
                cardholderName = cardHolderName.value!!,
                expMonth = _cardExpiryMonth.value!!,
                expYear = _cardExpiryYear.value!!,
                securityCode = cardSecurityCode.value!!
            )) { token: String? ->
                createTokenCallback(token)
            }
        }
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

    fun createTokenCallback(token: String?) {
        if (token == null) {
            formSubmitted.postValue(false)
            _status.postValue(ApiStatus.ERROR)
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val resp = donationRepository.charge(
                            Charge(
                                name = cardHolderName.value!!,
                                token = token,
                                amount = (amount.value!! * 100).toInt()
                            )
                        )
                        formSubmitted.postValue(false)
                        _status.postValue(ApiStatus.Success)
                    } catch (ex: Exception) {
                        when (ex) {
                            is IOException -> {
                                formSubmitted.postValue(false)
                                _status.postValue(ApiStatus.NoInternet)
                            }
                            else -> {
                                formSubmitted.postValue(false)
                                _status.postValue(ApiStatus.ERROR)
                            }
                        }
                    }
                }
            }
        }
    }

    fun doneNavigating() {
        _status.value = ApiStatus.NONE
    }
}