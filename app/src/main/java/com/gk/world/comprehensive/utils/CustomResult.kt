// Result.kt
package com.gk.world.comprehensive.utils

/**
 * 自定义Result类，替代Kotlin标准库的Result
 */
sealed class CustomResult<out T> {
    data class Success<out T>(val value: T) : CustomResult<T>()
    data class Failure(val exception: Exception) : CustomResult<Nothing>()

    val isSuccess: Boolean get() = this is Success<T>
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun exceptionOrNull(): Exception? = when (this) {
        is Success -> null
        is Failure -> exception
    }

    companion object {
        fun <T> success(value: T): CustomResult<T> = Success(value)
        fun <T> failure(exception: Exception): CustomResult<T> = Failure(exception)
        fun <T> failure(message: String): CustomResult<T> = Failure(Exception(message))
    }
}

/**
 * 用于网络请求的特定结果类
 */
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T, val message: String = "") : NetworkResult<T>()
    data class Error(val error: String, val exception: Exception? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    val isSuccess: Boolean get() = this is Success<T>
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading
}