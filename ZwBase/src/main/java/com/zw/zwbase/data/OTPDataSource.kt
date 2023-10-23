package com.zw.zwbase.data

import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.domain.OTPResponse

interface OTPDataSource {
    suspend fun uploadOTPFlow(otp:String): ApiResponse<OTPResponse>
}