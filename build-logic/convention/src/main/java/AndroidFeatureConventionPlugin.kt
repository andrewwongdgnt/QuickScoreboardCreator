import com.dgnt.quickScoreboardCreator.implementation
import com.dgnt.quickScoreboardCreator.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("quickScoreboardCreator.android.library")
                apply("quickScoreboardCreator.android.hilt")
            }

            dependencies {
                implementation(project(":core:presentation:designsystem"))
                implementation(project(":core:presentation:ui"))
                implementation(project(":core:domain"))

                implementation(libs.findLibrary("androidx-navigation-compose").get())
                implementation(libs.findLibrary("androidx-hilt-navigation-compose").get())
                implementation(libs.findLibrary("kotlinx-coroutines-android").get())

            }
        }
    }
}
