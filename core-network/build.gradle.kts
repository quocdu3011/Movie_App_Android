plugins {
    alias(libs.plugins.android.library)

    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.movieapp.core.network"
    compileSdk = 37
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.bundles.network)
    testImplementation(libs.junit)
    testImplementation(libs.mockwebserver)
}
