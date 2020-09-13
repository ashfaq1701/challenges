package com.omise.omisetest.screens.charities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.omise.omisetest.DonationApplication
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class CharitiesViewModel(application: DonationApplication): BaseViewModel(application) {
    @Inject
    lateinit var charitiesRepository:  CharitiesRepository

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _charities = MutableLiveData<List<Charity>>()
    val charities: LiveData<List<Charity>>
        get() = _charities

    init {
        getViewModelComponent().inject(this)

        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            withContext(Dispatchers.IO) {
                try {
                    _charities.postValue(charitiesRepository.getCharities())
                    _status.postValue(ApiStatus.DONE)
                } catch (ex: Exception) {
                    when (ex) {
                        is IOException -> {
                            _status.postValue(ApiStatus.NoInternet)
                        }
                        else -> {
                            _status.postValue(ApiStatus.ERROR)
                        }
                    }
                }
            }
        }
    }

    fun onCharityClicked(charityId: Int) {

    }
}