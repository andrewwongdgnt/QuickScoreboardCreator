plugins {
    alias(libs.plugins.quickScoreboardCreator.jvm.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.quickScoreboardCreator.test)
}

dependencies {

    api(projects.core.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.serialization.json)

    //other third party dependencies
    implementation(libs.joda.time)
    implementation(libs.guava)
    implementation(libs.gson.extras)
    implementation(libs.commons.lang3)
}