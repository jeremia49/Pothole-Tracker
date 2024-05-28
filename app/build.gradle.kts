plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "my.id.jeremia.potholetracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "my.id.jeremia.potholetracker"
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
        debug{
            buildConfigField("String","BASE_URL", "\"http://10.0.2.2:8000/\"")
//            buildConfigField("String","BASE_URL", "\"http://192.168.1.114:8000/\"")
        }
        release {
            buildConfigField("String","BASE_URL","\"https://potholetracker.jeremia.my.id\"")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        buildConfig = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}


dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    //Tensorflow
    implementation ("org.tensorflow:tensorflow-lite:2.10.0")
    implementation ("org.tensorflow:tensorflow-lite-support:0.4.4")

    //ROOM DB
    val room = "2.6.1"
    implementation("androidx.room:room-runtime:$room")
    ksp("androidx.room:room-compiler:$room")
    implementation("androidx.room:room-ktx:$room")


    // UI Image Cropper
    implementation("com.vanniktech:android-image-cropper:4.5.0")

    //Location
    implementation("com.google.android.gms:play-services-location:21.2.0")

    //Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    //CameraX
    val camerax_version = "1.3.2"
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation ("androidx.camera:camera-video:${camerax_version}")

    implementation ("androidx.camera:camera-view:${camerax_version}")
    implementation ("androidx.camera:camera-extensions:${camerax_version}")

    //Cryptography tool
    implementation("com.google.guava:guava:33.2.0-android")

    // Image Coil
    val coil = "2.6.0"
    implementation("io.coil-kt:coil:$coil")
    implementation("io.coil-kt:coil-compose:$coil")

    //Google Maps SDK
    implementation("com.google.maps.android:maps-ktx:5.0.0")
    implementation("com.google.maps.android:maps-compose:5.0.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Network
    val retrofit2 = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit2")
    implementation("com.squareup.retrofit2:converter-moshi:${retrofit2}")

    val okhttp3 = "4.12.0"
    implementation(platform("com.squareup.okhttp3:okhttp-bom:$okhttp3"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // JSON
    val moshi = "1.15.0"
    implementation("com.squareup.moshi:moshi:$moshi")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshi")


    val coroutines = "1.8.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")

    implementation(libs.androidx.lifecycle.runtime.compose)

    // Work
    val work = "2.9.0"
    implementation("androidx.work:work-runtime-ktx:$work")

    // Dependency Management
    val hilt = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt")
    ksp("com.google.dagger:hilt-android-compiler:$hilt")

    val hiltKtx = "1.1.0"
    implementation("androidx.hilt:hilt-navigation-compose:$hiltKtx")
    implementation("androidx.hilt:hilt-work:$hiltKtx")
    ksp("androidx.hilt:hilt-compiler:$hiltKtx")


    //Lottie Animation
    val lottie = "6.4.0"
    implementation("com.airbnb.android:lottie-compose:$lottie")


    implementation(libs.androidx.core.splashscreen)

    implementation(libs.material.icons.extended)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}