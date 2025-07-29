package com.shrichetanya.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.data.HomeRepository
import com.shrichetanya.model.LoginRequest
import com.shrichetanya.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: HomeRepository,
    application: Application
) : BaseViewModel(application) {

    val loginResponse: MutableLiveData<NetworkResult<LoginResponse>> = MutableLiveData()

    fun login(loginRequest: LoginRequest) = viewModelScope.launch {
        loginResponse.value = NetworkResult.Loading()
        repository.login(loginRequest).collect { values ->
            loginResponse.value = values
        }
    }
}