plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.history.di"
}

dependencies {

    implementation(projects.feature.history.domain)
    implementation(projects.feature.history.data)
    implementation(projects.core.database)
}


