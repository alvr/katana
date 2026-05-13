package dev.alvr.katana.shared.di

import androidx.lifecycle.ViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import kotlin.reflect.KClass

private typealias Provider<T> = Map<KClass<out T>, () -> T>

private typealias FactoryProvider<T, R> = Map<KClass<out T>, () -> R>

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
internal class KatanaViewModelFactory(
    override val viewModelProviders: Provider<ViewModel>,
    override val assistedFactoryProviders: FactoryProvider<ViewModel, ViewModelAssistedFactory>,
    override val manualAssistedFactoryProviders: Provider<ManualViewModelAssistedFactory>,
) : MetroViewModelFactory()
