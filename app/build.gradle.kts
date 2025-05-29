plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.augaluratas"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.augaluratas"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas".toString()
            }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/NOTICE.md",
                "META-INF/LICENSE.md" // optional
            )
        }
    }
}

dependencies {

    implementation(libs.legacy.support.v4)
    val room_version = "2.2.5"
    val lifecycle_version = "2.2.0"
    val arch_version = "2.1.0"

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.gridlayout)
    val camerax_version = "1.3.0"
    implementation ("androidx.camera:camera-core:$camerax_version")
    implementation ("androidx.camera:camera-camera2:$camerax_version")
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")
    implementation ("androidx.camera:camera-view:1.3.0")
    // Room biblioteka
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    androidTestImplementation("androidx.room:room-testing:$room_version")

    // Lifecycle komponentai
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
    androidTestImplementation("androidx.arch.core:core-testing:$arch_version")

    // Testavimo bibliotekos
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    //Notification biblioteka
    implementation("com.android.support:support-compat:28.0.0")

    //gson
    implementation ("com.google.code.gson:gson:2.11.0")

    //JavaMain API
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")


    //Needed for API requests
    implementation("com.google.android.gms:play-services-cronet:18.0.1")

}
