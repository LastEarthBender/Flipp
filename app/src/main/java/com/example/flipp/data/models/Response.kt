package com.example.flipp.data.models


sealed class Response<T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error<T>(val code: Int, val message: String) : Response<T>()
}
