plugins {
    alias(libs.plugins.quickScoreboardCreator.android.library)
    alias(libs.plugins.quickScoreboardCreator.android.room)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
}
android {
    namespace = "com.dgnt.quickScoreboardCreator.feature.history.data"
}

dependencies {
    implementation(projects.feature.history.domain)
    implementation(projects.feature.sport.domain)
    implementation(projects.feature.team.domain)
    api(projects.core.data)
    implementation(libs.joda.time)
    implementation(libs.gson.extras)
}