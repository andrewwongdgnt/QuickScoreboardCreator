plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.library.compose)
}
android {
    namespace = "com.dgnt.quickScoreboardCreator.core.presentation.designsystem"
}

dependencies {

    implementation(projects.core.presentation.ui)

}