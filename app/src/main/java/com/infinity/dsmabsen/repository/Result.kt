package com.infinity.dsmabsen.repository

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val value: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
}