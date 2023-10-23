package com.zw.zwbase.data.remote

import com.google.gson.JsonObject
import com.skydoves.sandwich.ApiResponse
import com.zw.zwbase.domain.OTPRequest
import com.zw.zwbase.domain.OTPResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OTPApiInterface {

    /*@POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<BaseResponse<LoginResponse>>*/

    @GET("update_otp/")
    suspend fun validateOTP(@Query("state") state : String,@Query("otp") otp: String): ApiResponse<OTPResponse>
    @POST("complaint_otp")
    suspend fun validateOTPPost(@Body body: OTPRequest): ApiResponse<OTPResponse>
}