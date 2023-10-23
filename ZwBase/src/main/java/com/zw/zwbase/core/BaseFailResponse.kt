/*
 * Copyright © 2023 Zetrixweb. All rights reserved.
 * Modify this class as per your requirement
 */
package com.zw.zwbase.core

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BaseFailResponse(
    @Json(name = "message") val message: String,
)
