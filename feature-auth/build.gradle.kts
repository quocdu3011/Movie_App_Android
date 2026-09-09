plugins {
    alias(libs.plugins.android.library)

}

android {
    namespace = "com.example.movieapp.feature.auth"
    compileSdk = 37
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-ui"))
    implementation(project(":core-common"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
}
