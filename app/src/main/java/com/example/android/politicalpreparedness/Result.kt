package com.example.android.politicalpreparedness

const val ERROR_OCCURRED = "Error Occurred"

sealed class Result<out T : Any> {
    object Fetching : Result<Nothing>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exceptionMessage: String = ERROR_OCCURRED) : Result<Nothing>()
}
