package dev.alvr.katana.core.ui.viewmodel

import androidx.compose.runtime.Immutable
import dev.alvr.katana.core.domain.failures.Failure
import kotlin.jvm.JvmInline

@Immutable
sealed interface SectionStatus<out T> {
    data object Uninitialized : SectionStatus<Nothing>

    data object Loading : SectionStatus<Nothing>

    @JvmInline value class Error(val failure: Failure) : SectionStatus<Nothing>

    @JvmInline value class Success<T>(val data: T) : SectionStatus<T>
}

val <T> SectionStatus<T>.dataOrNull
    get() =
        if (this is SectionStatus.Success) {
            data
        } else {
            null
        }

inline fun <T> SectionStatus<T>.onSuccess(block: (T) -> Unit): SectionStatus<T> = apply { dataOrNull?.let(block) }

inline fun <T, R> SectionStatus<T>.mapSuccess(transform: (T) -> R): SectionStatus<R> =
    when (this) {
        SectionStatus.Uninitialized -> SectionStatus.Uninitialized
        SectionStatus.Loading -> SectionStatus.Loading
        is SectionStatus.Error -> SectionStatus.Error(failure)
        is SectionStatus.Success -> SectionStatus.Success(transform(data))
    }
