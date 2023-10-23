package com.zw.zwbase.domain

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OTPResponse(@Json(name = "status") val status: Boolean?,
                  @Json(name = "message") val message: String?,
                  @Json(name = "data") val data: JsonElement?)