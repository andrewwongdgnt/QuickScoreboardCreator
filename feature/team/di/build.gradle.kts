plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.team.di"
}

dependencies {

    implementation(projects.feature.team.domain)
    implementation(projects.feature.team.data)
}
