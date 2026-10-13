package app.christianzz.muix

import app.christianzz.muix.shared.MuixColors
import app.christianzz.muix.shared.MuixDrawables
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.patcher.patch.booleanOption
import org.w3c.dom.Element

/**
 * Universal MUIX resource transformation engine.
 *
 * Intentionally **not** bound to any package name. Applies HyperOS / MIUI-inspired
 * visual tokens to common Material / AppCompat color and drawable resources.
 *
 * Realistic coverage:
 * - High: Material/AppCompat color resources, injected rounded drawables
 * - Medium: soft style injection for dialogs
 * - Low / none: Jetpack Compose, custom hard-coded Views, WebViews, native rendering
 *
 * All modifications are additive or in-place overrides of existing resource keys.
 * Missing files and unsupported structures are skipped without failing the patch.
 */
@Suppress("unused")
val muixResourcePatch = resourcePatch(
    name = "Universal MUIX",
    description = "Applies a HyperOS / MIUI-inspired visual language (MUIX) to common Material and AppCompat resources. " +
        "Rounded surfaces, modern accents, clean light/dark surfaces. Universal (no package restriction). " +
        "Does not fully restyle Compose-heavy or custom-drawn UIs.",
    default = false
) {
    // Options (documented Morphe API style)
    val accentColor by stringOption(
        key = "accentColor",
        default = "#FF0A84FF"
    )
    val cornerRadius by stringOption(
        key = "cornerRadius",
        default = "16"
    )
    val enableRoundedDrawables by booleanOption(
        key = "enableRoundedDrawables",
        default = true
    )
    val overridePrimaryColors by booleanOption(
        key = "overridePrimaryColors",
        default = true
    )
    val applySurfaceColors by booleanOption(
        key = "applySurfaceColors",
        default = true
    )

    execute {
        val radius = cornerRadius?.toIntOrNull()?.coerceIn(4, 32) ?: 16
        val accent = normalizeColor(accentColor) ?: MuixColors.ACCENT_LIGHT

        ensureMuixColorResources(accent)

        if (enableRoundedDrawables == true) {
            injectRoundedDrawables(radius)
        }

        if (overridePrimaryColors == true || applySurfaceColors == true) {
            patchExistingColorResources(
                accent = accent,
                overridePrimary = overridePrimaryColors == true,
                applySurfaces = applySurfaceColors == true
            )
        }

        softPatchStyles(radius)
    }
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

private fun normalizeColor(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    val cleaned = raw.trim().uppercase()
    return when {
        cleaned.matches(Regex("^#[0-9A-F]{6}$")) -> "#FF${cleaned.substring(1)}"
        cleaned.matches(Regex("^#[0-9A-F]{8}$")) -> cleaned
        else -> null
    }
}

private fun app.morphe.patcher.patch.ResourcePatchContext.ensureMuixColorResources(accent: String) {
    addOrUpdateColors(
        path = "res/values/colors.xml",
        entries = mapOf(
            "muix_accent" to accent,
            "muix_surface" to MuixColors.SURFACE_LIGHT,
            "muix_surface_elevated" to MuixColors.SURFACE_ELEVATED_LIGHT,
            "muix_background" to MuixColors.BACKGROUND_LIGHT,
            "muix_text_primary" to MuixColors.TEXT_PRIMARY_LIGHT,
            "muix_text_secondary" to MuixColors.TEXT_SECONDARY_LIGHT,
            "muix_outline" to MuixColors.OUTLINE_LIGHT,
            "muix_error" to MuixColors.ERROR,
            "muix_success" to MuixColors.SUCCESS,
            "muix_warning" to MuixColors.WARNING
        )
    )
    addOrUpdateColors(
        path = "res/values-night/colors.xml",
        entries = mapOf(
            "muix_accent" to accent,
            "muix_surface" to MuixColors.SURFACE_DARK,
            "muix_surface_elevated" to MuixColors.SURFACE_ELEVATED_DARK,
            "muix_background" to MuixColors.BACKGROUND_DARK,
            "muix_text_primary" to MuixColors.TEXT_PRIMARY_DARK,
            "muix_text_secondary" to MuixColors.TEXT_SECONDARY_DARK,
            "muix_outline" to MuixColors.OUTLINE_DARK,
            "muix_error" to MuixColors.ERROR,
            "muix_success" to MuixColors.SUCCESS,
            "muix_warning" to MuixColors.WARNING
        )
    )
}

private fun app.morphe.patcher.patch.ResourcePatchContext.addOrUpdateColors(
    path: String,
    entries: Map<String, String>
) {
    try {
        val file = get(path, true)
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.writeText(buildString {
                appendLine("""<?xml version="1.0" encoding="utf-8"?>""")
                appendLine("""<resources>""")
                entries.forEach { (name, value) ->
                    appendLine("""    <color name="$name">$value</color>""")
                }
                appendLine("""</resources>""")
            })
            return
        }

        document(path).use { doc ->
            val root = doc.documentElement ?: return@use
            val existing = mutableSetOf<String>()
            val children = root.childNodes
            for (i in 0 until children.length) {
                val node = children.item(i)
                if (node is Element && node.tagName == "color") {
                    val name = node.getAttribute("name")
                    if (name in entries) {
                        node.textContent = entries[name]
                        existing += name
                    }
                }
            }
            entries.filterKeys { it !in existing }.forEach { (name, value) ->
                val el = doc.createElement("color")
                el.setAttribute("name", name)
                el.textContent = value
                root.appendChild(el)
            }
        }
    } catch (_: Exception) {
        // Missing or protected resource — skip
    }
}

private fun app.morphe.patcher.patch.ResourcePatchContext.injectRoundedDrawables(radius: Int) {
    val drawables = mapOf(
        "res/drawable/muix_rounded_rect.xml" to MuixDrawables.roundedRect(radius),
        "res/drawable/muix_elevated_card.xml" to MuixDrawables.elevatedCard(radius),
        "res/drawable/muix_pill.xml" to MuixDrawables.pill(),
        "res/drawable/muix_dialog_bg.xml" to MuixDrawables.dialogBackground(radius + 8),
        "res/drawable/muix_rounded_selector.xml" to MuixDrawables.roundedSelector(radius - 4)
    )
    drawables.forEach { (path, content) ->
        try {
            val file = get(path, true)
            file.parentFile?.mkdirs()
            file.writeText(content)
        } catch (_: Exception) {
            // ignore
        }
    }
}

private fun app.morphe.patcher.patch.ResourcePatchContext.patchExistingColorResources(
    accent: String,
    overridePrimary: Boolean,
    applySurfaces: Boolean
) {
    val colorFiles = listOf(
        "res/values/colors.xml",
        "res/values-night/colors.xml",
        "res/values-v21/colors.xml",
        "res/values-v23/colors.xml",
        "res/values-v31/colors.xml"
    )

    val primaryMap = if (overridePrimary) {
        MuixColors.COMMON_PRIMARY_NAMES.associateWith { accent }
    } else emptyMap()

    val surfaceMap = if (applySurfaces) {
        mapOf(
            "colorSurface" to "@color/muix_surface_elevated",
            "colorSurfaceVariant" to "@color/muix_surface",
            "colorBackground" to "@color/muix_background",
            "background" to "@color/muix_background",
            "windowBackground" to "@color/muix_background"
        )
    } else emptyMap()

    val allOverrides = primaryMap + surfaceMap
    if (allOverrides.isEmpty()) return

    colorFiles.forEach { path ->
        try {
            val file = get(path)
            if (!file.exists()) return@forEach
            document(path).use { doc ->
                val root = doc.documentElement ?: return@use
                val children = root.childNodes
                for (i in 0 until children.length) {
                    val node = children.item(i)
                    if (node is Element && node.tagName == "color") {
                        val name = node.getAttribute("name")
                        allOverrides[name]?.let { node.textContent = it }
                    }
                }
            }
        } catch (_: Exception) {
            // skip
        }
    }
}

private fun app.morphe.patcher.patch.ResourcePatchContext.softPatchStyles(radius: Int) {
    val styleFiles = listOf(
        "res/values/styles.xml",
        "res/values-v21/styles.xml",
        "res/values-night/styles.xml"
    )
    styleFiles.forEach { path ->
        try {
            val file = get(path)
            if (!file.exists()) return@forEach
            document(path).use { doc ->
                val root = doc.documentElement ?: return@use
                val hasMuixTheme = (0 until root.childNodes.length).any { i ->
                    val n = root.childNodes.item(i)
                    n is Element && n.tagName == "style" && n.getAttribute("name") == "Muix.Dialog"
                }
                if (!hasMuixTheme) {
                    val style = doc.createElement("style")
                    style.setAttribute("name", "Muix.Dialog")
                    style.setAttribute("parent", "Theme.AppCompat.Dialog")
                    fun addItem(name: String, value: String) {
                        val item = doc.createElement("item")
                        item.setAttribute("name", name)
                        item.textContent = value
                        style.appendChild(item)
                    }
                    addItem("android:windowBackground", "@drawable/muix_dialog_bg")
                    addItem("android:backgroundDimAmount", "0.45")
                    root.appendChild(style)
                }
            }
        } catch (_: Exception) {
            // skip
        }
    }
}
