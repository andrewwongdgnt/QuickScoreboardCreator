plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.sport.di"
}

dependencies {

    implementation(projects.feature.sport.domain)
    implementation(projects.feature.sport.data)
    implementation(projects.core.database)
}