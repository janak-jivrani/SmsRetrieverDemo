package com.zw.zwbase.usecase

import com.google.gson.JsonObject
import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.data.OTPDataSource
import com.zw.zwbase.domain.OTPResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OtpTaskUseCase @Inject constructor(private val otpDataSource: OTPDataSource) {
    suspend fun invoke(otp:String): ApiResponse<OTPResponse> = otpDataSource.uploadOTPFlow(otp)
}
