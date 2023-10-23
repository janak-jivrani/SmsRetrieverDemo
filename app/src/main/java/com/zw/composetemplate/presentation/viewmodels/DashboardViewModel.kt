package com.zw.composetemplate.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.domain.OTPResponse
import com.zw.zwbase.usecase.OtpTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(val otpTaskUseCase: OtpTaskUseCase): ViewModel() {
    private val _initialSataLoad = MutableLiveData<Boolean>()
    val initialSataLoad : LiveData<Boolean> = _initialSataLoad

    private val _otpValue : MutableState<String> = mutableStateOf("")
    val otpValue : State<String> = _otpValue

    private val _otpResponse : MutableLiveData<ApiResponse<OTPResponse>> = MutableLiveData()
    val otpResponse : LiveData<ApiResponse<OTPResponse>> = _otpResponse

    fun setLoaded(isLoaded: Boolean) {
        _initialSataLoad.postValue(isLoaded)
    }

    fun updateOTP(it: String) {
        _otpValue.value = it
        if (it.length >= 6) {
            callOTPApi()
        }
    }

    fun callOTPApi() {
        viewModelScope.launch {
            _otpResponse.postValue(otpTaskUseCase.invoke(_otpValue.value))
        }
    }

}