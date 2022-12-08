package dev.alvr.katana.ui.base.navigation

import com.ramcosta.composedestinations.result.ResultBackNavigator

typealias MultiResultBackNavigator<R> = ResultBackNavigator<Pair<Int, R>>

fun <R> MultiResultBackNavigator<R>.navigateBack(
    code: Int,
    result: R,
    onlyIfResumed: Boolean = false
) {
    navigateBack(result = code to result, onlyIfResumed = onlyIfResumed)
}

fun <R> MultiResultBackNavigator<R>.setResult(code: Int, result: R) {
    setResult(result = code to result)
}
