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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime.livedata)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
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

