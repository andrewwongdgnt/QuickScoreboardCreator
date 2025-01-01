plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.scoreboard.di"
}

dependencies {

    implementation(projects.feature.scoreboard.domain)
    implementation(projects.feature.history.domain)
}