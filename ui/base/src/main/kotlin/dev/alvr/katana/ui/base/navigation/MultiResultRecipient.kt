package dev.alvr.katana.ui.base.navigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.spec.DestinationSpec

typealias MultiResultRecipient<D, R> = ResultRecipient<D, Pair<Int, R>>

@Composable
fun <D : DestinationSpec<*>, R> MultiResultRecipient<D, R>.OnBackResult(listener: ResultData<R>) {
    onNavResult { navResult ->
        when (navResult) {
            NavResult.Canceled -> Unit
            is NavResult.Value -> with(navResult.value) {
                listener(first, second)
            }
        }
    }
}

fun interface ResultData<R> {
    operator fun invoke(code: Int, result: R)
}
