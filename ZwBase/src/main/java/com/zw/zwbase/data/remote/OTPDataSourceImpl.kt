package com.zw.zwbase.data.remote

import com.google.gson.JsonObject
import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.data.OTPDataSource
import com.zw.zwbase.domain.OTPResponse
import javax.inject.Inject

class OTPDataSourceImpl @Inject constructor(val otpApiInterface: OTPApiInterface) : OTPDataSource {

    override suspend fun uploadOTPFlow(otp: String): ApiResponse<OTPResponse> {
        return otpApiInterface.validateOTP(complaint_id =  "111", otp = otp)
    }
}