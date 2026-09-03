import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless) apply false
}

// Resolved eagerly against the root project so it's available before each
// subproject's own build script (and its generated `libs` accessor) has run.
val versionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
val detektVersion = versionCatalog.findVersion("detekt").get().requiredVersion
val ktlintVersion = versionCatalog.findVersion("ktlint").get().requiredVersion

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    extensions.configure<DetektExtension> {
        toolVersion = detektVersion
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
        parallel = true
        autoCorrect = false
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = "17"
        reports {
            html.required.set(true)
            xml.required.set(true)
            sarif.required.set(true)
            txt.required.set(false)
            md.required.set(false)
        }
    }

    extensions.configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint(ktlintVersion)
            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(ktlintVersion)
        }
    }
}

tasks.register("detektAll") {
    group = "verification"
    description = "Runs Detekt across all subprojects."
    dependsOn(subprojects.map { "${it.path}:detekt" })
}

tasks.register("spotlessCheckAll") {
    group = "verification"
    description = "Runs Spotless check across all subprojects."
    dependsOn(subprojects.map { "${it.path}:spotlessCheck" })
}

tasks.register("spotlessApplyAll") {
    group = "formatting"
    description = "Applies Spotless formatting across all subprojects."
    dependsOn(subprojects.map { "${it.path}:spotlessApply" })
}
