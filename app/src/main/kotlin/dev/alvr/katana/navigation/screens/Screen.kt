package dev.alvr.katana.navigation.screens

import androidx.navigation.navArgument

internal sealed class Screen private constructor(
    private val baseRoute: String,
    private val requiredArgs: List<NavArg> = emptyList(),
    private val optionalArgs: List<NavArg> = emptyList(),
) {

    object Login : Screen("login")
    object Home : Screen("home") {
        object Lists : ChildScreen(Home, "lists")
        object Explore : ChildScreen(Home, "explore")
        object Social : ChildScreen(Home, "social")
        object Account : ChildScreen(Home, "account")
    }

    sealed class ChildScreen(
        parent: Screen,
        baseRoute: String,
        requiredArgs: List<NavArg> = emptyList(),
        optionalArgs: List<NavArg> = emptyList(),
    ) : Screen(
        baseRoute = "${parent.baseRoute}/$baseRoute",
        requiredArgs = requiredArgs,
        optionalArgs = optionalArgs,
    )

    val route = run {
        val required = requiredArgs.toRoute(separator = "/", prefix = "/") { "{${it.key}}" }
        val optional =
            optionalArgs.toRoute(separator = "&", prefix = "?") { "${it.key}={${it.key}}" }

        baseRoute + required + optional
    }

    val arguments = (requiredArgs + optionalArgs).map { arg ->
        navArgument(arg.key) { type = arg.navType }
    }

    private fun List<NavArg>.toRoute(
        separator: String,
        prefix: String,
        transform: (NavArg) -> CharSequence,
    ) = takeIf { isNotEmpty() }
        ?.joinToString(separator = separator, prefix = prefix, transform = transform)
        ?: NO_ARGUMENT

    companion object {
        private const val NO_ARGUMENT = ""
    }
}
