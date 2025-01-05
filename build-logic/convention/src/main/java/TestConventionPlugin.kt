

import com.dgnt.quickScoreboardCreator.libs
import com.dgnt.quickScoreboardCreator.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                testImplementation(libs.findLibrary("junit").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("mockito-core").get())
                testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }

}
