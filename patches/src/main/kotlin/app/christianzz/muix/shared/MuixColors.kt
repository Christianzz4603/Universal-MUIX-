package app.christianzz.muix.shared

/**
 * MUIX color tokens inspired by Xiaomi HyperOS / MIUI visual language.
 * Original values — not copied from proprietary Xiaomi assets.
 *
 * Design goals:
 * - Clean surfaces with subtle elevation
 * - Soft rounded corners (handled by drawables / styles)
 * - Adaptive light / dark
 * - Preserve semantic colors (error, warning, success) where possible
 */
object MuixColors {

    // Primary accent (HyperOS-like blue-teal family)
    const val ACCENT_LIGHT = "#FF0A84FF"
    const val ACCENT_DARK = "#FF0A84FF"

    // Surfaces
    const val SURFACE_LIGHT = "#FFF5F6F8"
    const val SURFACE_DARK = "#FF1C1C1E"
    const val SURFACE_ELEVATED_LIGHT = "#FFFFFFFF"
    const val SURFACE_ELEVATED_DARK = "#FF2C2C2E"

    // Backgrounds
    const val BACKGROUND_LIGHT = "#FFF2F3F5"
    const val BACKGROUND_DARK = "#FF000000"

    // Text
    const val TEXT_PRIMARY_LIGHT = "#FF1A1A1A"
    const val TEXT_PRIMARY_DARK = "#FFF5F5F5"
    const val TEXT_SECONDARY_LIGHT = "#FF8A8A8E"
    const val TEXT_SECONDARY_DARK = "#FF8E8E93"

    // Dividers / outlines
    const val OUTLINE_LIGHT = "#FFE5E5EA"
    const val OUTLINE_DARK = "#FF38383A"

    // Semantic (kept close to Material so existing states remain readable)
    const val ERROR = "#FFFF3B30"
    const val SUCCESS = "#FF34C759"
    const val WARNING = "#FFFF9500"

    // Common Material / AppCompat color resource names that many apps declare.
    // The resource patch will attempt to override these when present.
    val COMMON_PRIMARY_NAMES = listOf(
        "colorPrimary",
        "colorPrimaryDark",
        "colorAccent",
        "colorSecondary",
        "colorPrimaryVariant",
        "colorSecondaryVariant",
        "accent",
        "primary",
        "colorControlActivated",
        "colorControlHighlight"
    )

    val COMMON_SURFACE_NAMES = listOf(
        "colorSurface",
        "colorSurfaceVariant",
        "colorBackground",
        "background",
        "windowBackground",
        "colorOnSurface",
        "colorOnBackground"
    )
}
