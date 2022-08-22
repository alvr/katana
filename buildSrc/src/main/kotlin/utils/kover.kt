package utils

val koverIncludes = listOf("dev.alvr.katana.*")
val koverExcludes = listOf(
    // Anonymous
    "*$$*",

    // App
    "*.KatanaApp",
    "*.initializers.*",

    // Apollo
    "*.remote.*.adapter.*",
    "*.remote.*.fragment.*",
    "*.remote.*.selections.*",
    "*.remote.*.type.*",
    "*.remote.*.*Mutation*",
    "*.remote.*.*Query*",

    // Common
    "*.common.*",

    // Common Android
    "*.BuildConfig",
    "*.*Activity",
    "*.*Fragment",
    "*.base.*",
    "*.navigation.*",

    // Compose
    "*.*ComposableSingletons*",

    // Hilt
    "*.di.*",
    "*.*Hilt_*",
    "*.*HiltModules*",
    "*.*_Factory",

    // Ui
    "*.ui.*.components.*",
    "*.ui.*.view.*",
)

const val KOVER_MIN_COVERED_PERCENTAGE = 80
