
import com.dgnt.quickScoreboardCreator.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val bom = libs.findLibrary("coil.kt").get()
            pluginManager.apply {
                apply("quickScoreboardCreator.android.library")
                apply("quickScoreboardCreator.android.hilt")
            }

            dependencies {
                add("implementation", project(":designsystem"))
                add("implementation", project(":ui"))
                add("implementation", project(":domain"))

                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("coil.kt").get())
                add("implementation", libs.findLibrary("coil.kt.compose").get())
                add("implementation", libs.findLibrary("compose.navigation").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())

            }
        }
    }
}
