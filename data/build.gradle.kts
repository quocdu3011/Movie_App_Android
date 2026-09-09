plugins {
    alias(libs.plugins.android.library)

}

android {
    namespace = "com.example.movieapp.data"
    compileSdk = 37
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-network"))
    implementation(project(":core-database"))
    implementation(project(":core-common"))
}
