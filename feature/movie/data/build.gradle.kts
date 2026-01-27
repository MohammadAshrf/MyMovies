plugins {
    alias(libs.plugins.mymovies.android.library)
}

android {
    namespace = "com.example.movie.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.work)
    implementation(libs.koin.android.workmanager)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.feature.movie.domain)
    implementation(projects.feature.movie.database)
}