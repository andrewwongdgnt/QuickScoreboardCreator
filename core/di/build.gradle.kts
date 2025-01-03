plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.room)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.core.di"
}

dependencies {
    implementation(projects.feature.history.data)
    implementation(projects.feature.sport.data)
    implementation(projects.feature.team.data)
    implementation(projects.core.database)
}