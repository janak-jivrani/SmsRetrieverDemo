package com.zw.zwbase.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OTPRequest(@Json(name = "complaint_id") val complaint_id: String,
                      @Json(name = "otp") val otp: String)