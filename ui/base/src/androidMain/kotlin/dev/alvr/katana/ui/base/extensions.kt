package dev.alvr.katana.ui.base

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@Composable
fun <R> ResultRecipient<*, R>.OnNavValue(onResult: (R) -> Unit) {
    onNavResult { result ->
        when (result) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> onResult(result.value)
        }
    }
}
