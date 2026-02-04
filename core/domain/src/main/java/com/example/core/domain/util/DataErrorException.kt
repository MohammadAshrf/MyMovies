package com.example.core.domain.util

class DataErrorException(
    val error: DataError
): Exception()