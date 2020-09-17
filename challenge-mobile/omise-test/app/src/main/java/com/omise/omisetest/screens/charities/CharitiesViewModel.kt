package com.omise.omisetest.screens.charities

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CharitiesViewModel(application: DonationApplication, val charitiesRepository: CharitiesRepository): AndroidViewModel(application) {
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _charities = MutableLiveData<List<Charity>>()
    val charities: LiveData<List<Charity>>
        get() = _charities

    private val _selectedCharity = MutableLiveData<Charity?>()
    val selectedCharity: LiveData<Charity?>
        get() = _selectedCharity

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _readyToNavigate = MutableLiveData<Boolean>(false)
    val readyToNavigate: LiveData<Boolean>
        get() = _readyToNavigate

    private val _navigateToConnectionError = MutableLiveData<Boolean>(false)
    val navigateToConnectionError: MutableLiveData<Boolean>
        get() = _navigateToConnectionError

    private val _navigateToServerError = MutableLiveData<Boolean>(false)
    val navigateToServerError: MutableLiveData<Boolean>
        get() = _navigateToServerError

    private val _navigateToDonation = MutableLiveData<Boolean>(false)
    val navigateToDonation: LiveData<Boolean>
        get() = _navigateToDonation

    fun loadCharities() {
        startLoading()
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            withContext(Dispatchers.IO) {
                try {
                    _charities.postValue(charitiesRepository.getCharities())
                    _status.postValue(ApiStatus.Success)
                    stopLoading()
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> {
                            _status.postValue(ApiStatus.NoInternet)
                            _navigateToConnectionError.postValue(true)
                            stopLoading()
                        }
                        else -> {
                            _status.postValue(ApiStatus.ERROR)
                            _navigateToServerError.postValue(true)
                            stopLoading()
                        }
                    }
                }
            }
        }
    }

    fun onCharityClicked(charityId: Int) {
        val filteredCharities = _charities.value?.filter {
            it.id == charityId
        }
        filteredCharities?.let {
            if (filteredCharities.isNotEmpty()) {
                _selectedCharity.value = filteredCharities.first()
                _navigateToDonation.value = true
            }
        }
    }

    private fun startLoading() {
        _isLoading.postValue(true)
    }

    private fun stopLoading() {
        _isLoading.postValue(false)
    }

    fun resetNavigateToServerError() {
        _navigateToServerError.postValue(false)
    }

    fun navigateToConnectionError() {
        _navigateToConnectionError.postValue(false)
    }

    fun resetNavigateToDonation() {
        _navigateToDonation.postValue(false)
    }
}