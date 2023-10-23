/*
 * Copyright © 2023 Zetrixweb. All rights reserved.
 * Modify this class as per your requirement
 */
package com.zw.zwbase.core

typealias EmptyResource = Resource<Unit>

sealed class Resource<T> {
    data class Success<T>(val data: T?) : Resource<T>() {
        override fun toString() = "[Success: $data]"
    }

    // Optional data allows to expose data stub just for loading state.
    data class Loading<T>(val data: T? = null) : Resource<T>() {
        override fun toString() = "[Loading: $data]"
    }

    data class Failure<T>(val error: Error) : Resource<T>() {
        override fun toString() = "[Failure: $error]"
    }

    class None<T> : Resource<T>() {
        override fun toString() = "[None]"
    }

    fun unwrap(): T? =
        when (this) {
            is Loading -> data
            is Success -> data
            is None -> null
            is Failure -> null
        }

    fun failure(): Error? =
        when (this) {
            is Failure -> error
            else -> null
        }

    inline fun onFailure(handle: (Error) -> Unit): Resource<T> {
        if (this is Failure) {
            handle(error)
        }
        return this
    }
}

fun <T> T?.success() = Resource.Success(this)
fun <T> T?.loading() = Resource.Loading(this)
fun <T> loading() = Resource.Loading(null)
fun <T> none() = Resource.None<T>()
