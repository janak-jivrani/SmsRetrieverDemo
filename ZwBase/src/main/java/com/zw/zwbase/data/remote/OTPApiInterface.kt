package com.zw.zwbase.data.remote

import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.domain.OTPResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OTPApiInterface {

    /*@POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<BaseResponse<LoginResponse>>*/

    @GET("complaint_otp")
    suspend fun validateOTP(@Query("complaint_id") complaint_id : String,@Query("otp") otp: String): ApiResponse<OTPResponse>
}