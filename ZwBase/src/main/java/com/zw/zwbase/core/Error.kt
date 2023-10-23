/*
 * Copyright © 2023 Zetrixweb. All rights reserved.
 * Modify this class as per your requirement
 */
package com.zw.zwbase.core

import com.skydoves.sandwich.ApiResponse

fun Throwable.asError() = Error.Exception(this)

fun <T> ApiResponse.Failure.Error<T>.asError(): Error.FromApi {
    return Error.FromApi(this.raw.request.url.encodedPath,
        this.statusCode.code,
        this.errorBody?.toMessage())
}

fun <T> ApiResponse.Failure.Exception<T>.asError() = exception.asError()

sealed class Error(message: String?, open val code: Int?, clause: Throwable?) : Throwable(message, clause) {
    data class FromApi(val requestPath: String, override val code: Int, override val message: String?) :
        Error(message, code, null) {
        override fun toString() = "[Error.Api: $requestPath]"
    }

    data class Exception(val exception: Throwable) : Error(exception.message, null, exception) {
        override fun toString() = "[Error.Exception: $exception]"
    }
}
