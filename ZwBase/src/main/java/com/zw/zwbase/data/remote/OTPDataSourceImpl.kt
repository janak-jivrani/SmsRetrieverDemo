package com.zw.zwbase.data.remote

import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.data.OTPDataSource
import com.zw.zwbase.domain.OTPRequest
import com.zw.zwbase.domain.OTPResponse
import javax.inject.Inject

class OTPDataSourceImpl @Inject constructor(val otpApiInterface: OTPApiInterface) : OTPDataSource {

    override suspend fun uploadOTPFlow(otp: String): ApiResponse<OTPResponse> {
        //@Query("complaint_id") complaint_id : String,@Query("otp") otp: String
        return otpApiInterface.validateOTP(state =  "mh", otp = otp)
        //val otpRequest = OTPRequest(complaint_id = "111",otp = otp)
        //return otpApiInterface.validateOTPPost(otpRequest)
    }

}