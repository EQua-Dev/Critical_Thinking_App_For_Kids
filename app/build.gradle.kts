plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("com.google.gms.google-services")
    id ("androidx.navigation.safeargs.kotlin")
    id ("kotlin-parcelize")
    id ("kotlin-android")
    kotlin("kapt")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.awesomenessstudios.schoolprojects.criticalthinkingappforkids"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.awesomenessstudios.schoolprojects.criticalthinkingappforkids"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.firebase.bom)
    implementation (libs.firebase.analytics.ktx)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.google.firebase.core)
    implementation (libs.firebase.firestore.ktx)
    implementation (libs.firebase.ui.firestore)
    //implementation (libs.play.services.auth) // Check for the latest version

    implementation (libs.androidx.multidex)

    //Dagger - Hilt
    //implementation ("com.google.dagger:hilt-android:2.43.2")
    //kapt("com.google.dagger:hilt-android-compiler:2.43.2")
    //kapt("androidx.hilt:hilt-compiler:1.0.0")
    //implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    //Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    //May need okkhttp also

    // Dagger - Hilt
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)


    // Timber
    //def timber_version = "5.0.1"
    //implementation("com.jakewharton.timber:timber:5.0.1")


    //Kotlin Coroutines
    implementation (libs.kotlinx.coroutines.play.services)
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    //Navigation Component
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    //Data Store
    implementation (libs.androidx.datastore.preferences)

    //Lifecycle
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.lifecycle.runtime.ktx.v284)

    //Lottie Animation
    implementation (libs.lottie)
    implementation (libs.lottiedialog)
    implementation (libs.lottie.compose)

    implementation (libs.androidx.material.icons.extended)

    //run-time permission manager
    implementation (libs.dexter)

}