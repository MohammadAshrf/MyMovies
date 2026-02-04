package com.example.core.presentation.ui.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.core.domain.util.DataErrorException
import com.example.core.presentation.ui.R

sealed interface UiText {
    data class DynamicString(val value: String): UiText
    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> stringResource(id = id, *args)
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}

fun Throwable.asUiText(): UiText {
    return when(this) {
        is DataErrorException -> this.error.asUiText()

        is java.io.IOException -> UiText.StringResource(R.string.error_no_internet)

        else -> UiText.StringResource(R.string.error_unknown)
    }
}