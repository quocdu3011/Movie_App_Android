plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.movieapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.movieapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(project(":core-ui"))
    implementation(project(":core-common"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core-network"))
    implementation(project(":core-database"))
    implementation(project(":core-player"))
    implementation(project(":feature-auth"))
    implementation(project(":feature-home"))
    implementation(project(":feature-detail"))
    implementation(project(":feature-player"))
    implementation(project(":feature-search"))
    implementation(project(":feature-profile"))
    implementation(project(":feature-subscription"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}