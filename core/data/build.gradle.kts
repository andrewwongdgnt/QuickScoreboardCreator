plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.room)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.joda.time)
    implementation(libs.gson.extras)
}