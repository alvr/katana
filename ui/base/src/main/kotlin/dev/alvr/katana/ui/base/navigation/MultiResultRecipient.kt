package dev.alvr.katana.ui.base.navigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.spec.DestinationSpec
import dev.alvr.katana.ui.base.OnNavValue

typealias MultiResultRecipient<D, R> = ResultRecipient<D, Pair<Int, R>>

@Composable
fun <D : DestinationSpec<*>, R> MultiResultRecipient<D, R>.OnBackResult(listener: ResultData<R>) {
    OnNavValue { (code, result) -> listener(code, result) }
}

fun interface ResultData<R> {
    operator fun invoke(code: Int, result: R)
}
