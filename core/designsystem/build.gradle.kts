plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.library.compose)
}
android {
    namespace = "com.dgnt.quickScoreboardCreator.core.designsystem"
}

dependencies {

    implementation(libs.androidx.core.ktx)

}