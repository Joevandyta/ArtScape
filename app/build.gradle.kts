plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
}

android {
    namespace = "com.jovan.artscape"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jovan.artscape"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "ENDPOINT_REGION", "\"https://www.emsifa.com/api-wilayah-indonesia/api/\"")
            buildConfigField("String", "ENDPOINT_ARTSCAPE", "\"https://backend-app-artscape-7yazuf5wiq-et.a.run.app/\"")

        }
        release {
            buildConfigField("String", "ENDPOINT_REGION", "\"https://www.emsifa.com/api-wilayah-indonesia/api/\"")
            buildConfigField("String", "ENDPOINT_ARTSCAPE", "\"https://backend-app-artscape-7yazuf5wiq-et.a.run.app/\"")

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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //coroutine
    implementation(libs.jetbrains.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //googlePlayServices
    implementation(libs.play.services.base)

    implementation(libs.github.glide )
//    dataStore
    implementation(libs.androidx.datastore.preferences)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    implementation(libs.logging.interceptor)

    //    credential
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

//    Firebase
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    //ui
    implementation(libs.lottie)
}