
import com.dgnt.quickScoreboardCreator.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("quickScoreboardCreator.android.library")
                apply("quickScoreboardCreator.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:presentation:designsystem"))
                add("implementation", project(":core:presentation:ui"))
                add("implementation", project(":core:domain"))

                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))


                add("implementation", libs.findLibrary("androidx-navigation-compose").get())
                add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
                add("implementation", libs.findLibrary("kotlinx-coroutines-android").get())

            }
        }
    }
}
