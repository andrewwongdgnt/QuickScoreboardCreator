plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library.compose)
    alias(libs.plugins.quickScoreboardCreator.android.feature)
    alias(libs.plugins.quickScoreboardCreator.test)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.history.presentation"
}

dependencies {

    implementation(projects.feature.history.domain)
    implementation(projects.feature.sport.presentation)
    implementation(projects.feature.sport.domain)
    implementation(projects.feature.team.domain)

    //chart library
    implementation(libs.mpandroidchart)

    //other third party dependencies
    implementation(libs.joda.time)
}