plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library.compose)
    alias(libs.plugins.quickScoreboardCreator.android.feature)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.team.presentation"
}

dependencies {

    implementation(projects.feature.team.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.util)
    implementation(libs.commons.lang3)
}