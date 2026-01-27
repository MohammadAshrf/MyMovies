package com.example.core.domain.util

sealed interface Result<out Data, out E: Error> {
    data class Success<out Data>(val data: Data): Result<Data, Nothing>
    data class Failure<out E: Error>(val error: E): Result<Nothing, E>
}

inline fun <Type, E: Error, AnotherType> Result<Type, E>.map(map: (Type) -> AnotherType): Result<AnotherType, E> {
    return when(this) {
        is Result.Failure -> Result.Failure(error)
        is Result.Success -> Result.Success(map(this.data))
    }
}

inline fun <Type, E: Error> Result<Type, E>.onSuccess(action: (Type) -> Unit): Result<Type, E> {
    return when(this) {
        is Result.Failure -> this
        is Result.Success -> {
            action(this.data)
            this
        }
    }
}

inline fun <Type, E: Error> Result<Type, E>.onFailure(action: (E) -> Unit): Result<Type, E> {
    return when(this) {
        is Result.Failure -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

fun <Type, E: Error> Result<Type, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

typealias EmptyResult<E> = Result<Unit, E>