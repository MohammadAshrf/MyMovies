plugins {
    alias(libs.plugins.mymovies.android.library)
    alias(libs.plugins.mymovies.jvm.ktor)
}

android {
    namespace = "com.example.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
}