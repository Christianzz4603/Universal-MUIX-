package app.christianzz.muix.shared

/**
 * XML shape templates used by the MUIX resource patch.
 * All shapes are original and recreate a modern rounded aesthetic.
 */
object MuixDrawables {

    /**
     * Standard rounded rectangle used for cards, buttons, text fields, dialogs.
     * @param cornerRadiusDp corner radius in dp (default HyperOS-like 16)
     * @param solidColor color reference or hex
     */
    fun roundedRect(
        cornerRadiusDp: Int = 16,
        solidColor: String = "@color/muix_surface_elevated"
    ): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <shape xmlns:android="http://schemas.android.com/apk/res/android"
            android:shape="rectangle">
            <solid android:color="$solidColor" />
            <corners android:radius="${cornerRadiusDp}dp" />
        </shape>
    """.trimIndent()

    /**
     * Soft elevated card with slight stroke for light theme.
     */
    fun elevatedCard(
        cornerRadiusDp: Int = 16,
        solidColor: String = "@color/muix_surface_elevated",
        strokeColor: String = "@color/muix_outline"
    ): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <shape xmlns:android="http://schemas.android.com/apk/res/android"
            android:shape="rectangle">
            <solid android:color="$solidColor" />
            <corners android:radius="${cornerRadiusDp}dp" />
            <stroke
                android:width="0.5dp"
                android:color="$strokeColor" />
        </shape>
    """.trimIndent()

    /**
     * Pill / fully rounded button background.
     */
    fun pill(
        solidColor: String = "@color/muix_accent"
    ): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <shape xmlns:android="http://schemas.android.com/apk/res/android"
            android:shape="rectangle">
            <solid android:color="$solidColor" />
            <corners android:radius="999dp" />
        </shape>
    """.trimIndent()

    /**
     * Dialog background with large radius.
     */
    fun dialogBackground(
        cornerRadiusDp: Int = 24,
        solidColor: String = "@color/muix_surface_elevated"
    ): String = roundedRect(cornerRadiusDp, solidColor)

    /**
     * Simple ripple-friendly selector that falls back to a solid rounded shape.
     * Many apps expect a selector; this provides a safe base.
     */
    fun roundedSelector(
        cornerRadiusDp: Int = 12,
        normalColor: String = "@color/muix_surface_elevated",
        pressedColor: String = "@color/muix_accent"
    ): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <selector xmlns:android="http://schemas.android.com/apk/res/android">
            <item android:state_pressed="true">
                <shape android:shape="rectangle">
                    <solid android:color="$pressedColor" />
                    <corners android:radius="${cornerRadiusDp}dp" />
                </shape>
            </item>
            <item>
                <shape android:shape="rectangle">
                    <solid android:color="$normalColor" />
                    <corners android:radius="${cornerRadiusDp}dp" />
                </shape>
            </item>
        </selector>
    """.trimIndent()
}
