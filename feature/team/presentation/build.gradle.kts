plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library.compose)
    alias(libs.plugins.quickScoreboardCreator.android.feature)
    alias(libs.plugins.quickScoreboardCreator.test)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.team.presentation"
}

dependencies {

    implementation(projects.feature.team.domain)
    api(projects.core.util)//TODO do i need this
    implementation(libs.commons.lang3)//TODO do i need this
}