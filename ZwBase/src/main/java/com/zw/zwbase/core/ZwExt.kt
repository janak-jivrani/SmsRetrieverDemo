/*
 * Copyright © 2023 Zetrixweb. All rights reserved.
 * Modify this class as per your requirement
 */
package com.zw.zwbase.core

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

fun ResponseBody.toMessage(): String {
    val json: String = this.string()

    val moshi: Moshi = Moshi.Builder().build()
    val jsonAdapter: JsonAdapter<BaseFailResponse> = moshi.adapter(BaseFailResponse::class.java)

    val failResponse: String = try {
        jsonAdapter.fromJson(json)?.message ?: ""
    } catch (e: Exception) {
        "Something went wrong!"
    }
    return failResponse
}