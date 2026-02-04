package com.example.core.presentation.ui.util

import com.example.core.domain.util.DataError
import com.example.core.presentation.ui.R

fun DataError.asUiText(): UiText {
    return when(this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )
        DataError.Remote.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.error_request_timeout
        )
        DataError.Remote.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.error_too_many_requests
        )
        DataError.Remote.NO_INTERNET -> UiText.StringResource(
            R.string.error_no_internet
        )
        DataError.Remote.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.error_payload_too_large
        )
        DataError.Remote.SERVER_ERROR -> UiText.StringResource(
            R.string.error_server_error
        )
        DataError.Remote.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )
        else -> UiText.StringResource(R.string.error_unknown)
    }
}