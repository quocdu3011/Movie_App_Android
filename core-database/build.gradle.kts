plugins {
    alias(libs.plugins.android.library)

}

android {
    namespace = "com.example.movieapp.core.database"
    compileSdk = 37
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.bundles.room)
    annotationProcessor(libs.androidx.room.compiler)
}
