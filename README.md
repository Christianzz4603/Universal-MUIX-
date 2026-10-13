# Universal MUIX

**HyperOS / MIUI-inspired UI transformation patches for the Morphe framework.**

Universal MUIX applies a modern Xiaomi HyperOS-style visual language to compatible Android applications without hard-coding package names. It targets common Material Components and AppCompat resources so the same patch set can be used across many apps.

## What it does

- Injects MUIX color tokens (accent, surfaces, backgrounds, text, outlines) for light and dark themes.
- Optionally overrides common Material / AppCompat primary and accent color resources.
- Injects original rounded shape drawables (`muix_rounded_rect`, `muix_elevated_card`, `muix_pill`, `muix_dialog_bg`, …).
- Softly improves dialog styling when styles resources are present.
- Configurable accent color, corner radius, and which layers to apply.

## What it does **not** do

- It is **not** a universal guarantee. Apps built primarily with Jetpack Compose, custom Views that hard-code colors, WebViews, or native rendering pipelines will receive only partial or no visual change.
- It never touches authentication, payment, licensing, or security-sensitive logic.
- It does not delete original resources or break click listeners / accessibility when the resource keys it targets are present.

## How to use

1. Install [Morphe Manager](https://morphe.software).
2. Add this repository as a patch source:
   ```
   https://github.com/Christianzz4603/Universal-MUIX-
   ```
   or open: https://morphe.software/add-source?github=Christianzz4603/Universal-MUIX-
3. Select any app, enable **Universal MUIX**, configure options if desired, and patch.
4. Install the resulting APK.

Alternatively use [Morphe Desktop](https://github.com/MorpheApp/morphe-desktop) with the generated `.mpp` file.

## Building from source

```bash
./gradlew buildAndroid
```

The patch bundle is produced at:

```
patches/build/libs/patches-*.mpp
```

## Project identity

- **Name**: Universal MUIX
- **Author**: Christianzz4603
- **License**: GPLv3 (see LICENSE and NOTICE)
- **Compatible with**: Morphe patcher / Manager / Desktop

This project is **not** affiliated with Xiaomi or the Morphe open-source project. The visual style is an original recreation of the general HyperOS aesthetic.

## Supported components (realistic)

| Component type              | Support level | Notes                                      |
|----------------------------|---------------|--------------------------------------------|
| Material / AppCompat colors| High          | colorPrimary, colorAccent, surfaces, etc.  |
| XML drawables / shapes     | High          | Injected muix_* drawables                  |
| Theme / style resources    | Medium        | Soft dialog style injection                |
| Layout XML attributes      | Low–Medium    | Only when resources reference overridable colors |
| Jetpack Compose            | Low           | Code-driven theming; mostly unaffected     |
| Custom Views / hard-coded  | None          | Bytecode colors remain                     |
| WebView / native           | None          | Out of scope                               |

## License

GNU General Public License v3.0 — see LICENSE and NOTICE.
