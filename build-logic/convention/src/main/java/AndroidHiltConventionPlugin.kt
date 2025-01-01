
import com.dgnt.quickScoreboardCreator.androidTestImplementation
import com.dgnt.quickScoreboardCreator.implementation
import com.dgnt.quickScoreboardCreator.ksp
import com.dgnt.quickScoreboardCreator.libs
import com.dgnt.quickScoreboardCreator.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("com.google.devtools.ksp")
            }

            dependencies {
                implementation(libs.findLibrary("hilt-android").get())
                ksp(libs.findLibrary("hilt-android-compiler").get())
                androidTestImplementation(libs.findLibrary("hilt-android-testing").get())
                testImplementation(libs.findLibrary("hilt-android-testing").get())
                implementation(libs.findLibrary("androidx-hilt-navigation-compose").get())
            }

        }
    }

}