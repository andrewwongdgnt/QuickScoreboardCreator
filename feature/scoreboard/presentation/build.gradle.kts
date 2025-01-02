plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library.compose)
    alias(libs.plugins.quickScoreboardCreator.android.feature)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation"
}

dependencies {

    implementation(projects.feature.scoreboard.domain)
    implementation(projects.feature.team.presentation)
    implementation(projects.feature.team.domain)
    implementation(projects.feature.sport.presentation)
    implementation(projects.feature.sport.domain)
    implementation(projects.feature.history.domain)
    api(projects.core.util)//TODO do i need this?
    //other third party dependencies
    implementation(libs.joda.time)
    implementation(libs.commons.lang3)
}