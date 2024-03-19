package dev.alvr.katana.core.ui.decompose

import com.arkivanov.decompose.GenericComponentContext
import org.koin.core.component.KoinScopeComponent

interface AppComponentContext : GenericComponentContext<AppComponentContext>, KoinScopeComponent
