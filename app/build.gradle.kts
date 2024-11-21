plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    id("quickScoreboardCreator.android.application")
    id("quickScoreboardCreator.android.application.compose")
    id("quickScoreboardCreator.android.hilt")
    kotlin("kapt")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.runtime.livedata)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.navigation.compose)
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

    //dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    //Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    //chart library
    implementation(libs.mpandroidchart)

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

