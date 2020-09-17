package com.omise.omisetest.screens.donations

import androidx.lifecycle.*
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charge
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.common.models.CreditCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DonationViewModel(application: DonationApplication, val donationRepository: DonationRepository, val charity: Charity): AndroidViewModel(application) {
    private val _cardNumber = MutableLiveData<String>()
    val cardHolderName = MutableLiveData<String>()
    private val _cardExpiryMonth = MutableLiveData<Int>()
    private val _cardExpiryYear = MutableLiveData<Int>()
    val cardSecurityCode = MutableLiveData<String>()
    val amountTxt = MutableLiveData<String>()
    private val amount: LiveData<Float> = Transformations.map(amountTxt) {
        if (it.isEmpty()) {
            0F
        } else {
            it.toFloat()
        }
    }
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val formFieldComplexLiveData = CreditCardComplexLiveData(_cardNumber, cardHolderName, _cardExpiryMonth, _cardExpiryYear, cardSecurityCode, amount)
    private val formValid: LiveData<Boolean> = Transformations.switchMap(formFieldComplexLiveData) { liveData ->
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

    private val _navigateToPaymentSuccessfulScreen = MutableLiveData<Boolean>(false)
    val navigateToPaymentSuccessfulScreen: MutableLiveData<Boolean>
        get() = _navigateToPaymentSuccessfulScreen

    private val _navigateToConnectionErrorScreen = MutableLiveData<Boolean>(false)
    val navigateToConnectionErrorScreen: MutableLiveData<Boolean>
        get() = _navigateToConnectionErrorScreen

    private val _navigateToServerErrorScreen = MutableLiveData<Boolean>(false)
    val navigateToServerErrorScreen: MutableLiveData<Boolean>
        get() = _navigateToServerErrorScreen

    private val _showProgressBar = MutableLiveData<Boolean>(false)
    val showProgressBar: MutableLiveData<Boolean>
        get() = _showProgressBar

    fun submitForm() {
        formSubmitted.value = true
        _showProgressBar.value = true
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

    private fun createTokenCallback(token: String?) {
        if (token == null) {
            formSubmitted.postValue(false)
            _showProgressBar.postValue(false)
            _status.postValue(ApiStatus.ERROR)
            _navigateToServerErrorScreen.postValue(true)
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        donationRepository.charge(
                            Charge(
                                name = cardHolderName.value!!,
                                token = token,
                                amount = (amount.value!! * 100).toInt()
                            )
                        )
                        formSubmitted.postValue(false)
                        _showProgressBar.postValue(false)
                        _status.postValue(ApiStatus.Success)
                        _navigateToPaymentSuccessfulScreen.postValue(true)
                    } catch (ex: Exception) {
                        when (ex) {
                            is IOException -> {
                                formSubmitted.postValue(false)
                                _showProgressBar.postValue(false)
                                _status.postValue(ApiStatus.NoInternet)
                                _navigateToConnectionErrorScreen.postValue(true)
                            }
                            else -> {
                                formSubmitted.postValue(false)
                                _showProgressBar.postValue(false)
                                _status.postValue(ApiStatus.ERROR)
                                _navigateToServerErrorScreen.postValue(true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun resetNavigateToPaymentSuccessfulScreen() {
        _navigateToPaymentSuccessfulScreen.postValue(false)
    }

    fun resetNavigateToConnectionErrorScreen() {
        _navigateToConnectionErrorScreen.postValue(false)
    }

    fun resetNavigateToServerErrorScreen() {
        _navigateToServerErrorScreen.postValue(false)
    }
}