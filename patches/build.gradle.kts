group = "app.christianzz.muix"

patches {
    about {
        name = "Universal MUIX"
        description = "Universal HyperOS / MIUI-inspired UI transformation for compatible Android apps. Applies modern rounded surfaces, Xiaomi-style accents, improved spacing and Material component styling while preserving original app functionality."
        source = "https://github.com/Christianzz4603/Universal-MUIX-"
        author = "Christianzz4603"
        contact = "na"
        website = "https://github.com/Christianzz4603/Universal-MUIX-"
        license = "GPLv3"
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
