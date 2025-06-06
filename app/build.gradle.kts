plugins {
    alias(libs.plugins.quickScoreboardCreator.android.application)
    alias(libs.plugins.quickScoreboardCreator.android.application.compose)
    alias(libs.plugins.quickScoreboardCreator.android.hilt)
    alias(libs.plugins.quickScoreboardCreator.android.room)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.dgnt.quickScoreboardCreator"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.util)

    implementation(projects.feature.history.data)
    implementation(projects.feature.history.di)
    implementation(projects.feature.history.domain)
    implementation(projects.feature.history.presentation)

    implementation(projects.feature.scoreboard.domain)
    implementation(projects.feature.scoreboard.di)
    implementation(projects.feature.scoreboard.presentation)

    implementation(projects.feature.sport.data)
    implementation(projects.feature.sport.di)
    implementation(projects.feature.sport.domain)
    implementation(projects.feature.sport.presentation)

    implementation(projects.feature.team.data)
    implementation(projects.feature.team.di)
    implementation(projects.feature.team.domain)
    implementation(projects.feature.team.presentation)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.kotlinx.serialization.json)


    //other third party dependencies
    implementation(libs.joda.time)
    implementation(libs.guava)
    implementation(libs.gson.extras)
    implementation(libs.commons.lang3)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    //ICU
    implementation(libs.icu4j)

    //junit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    //mockito
    testImplementation(libs.mockk)
    testImplementation(libs.mockito.core)

    //espresso
    androidTestImplementation(libs.androidx.espresso.core)

}

