plugins {
    alias(libs.plugins.mymovies.android.library)
    alias(libs.plugins.mymovies.android.room)
}

android {
    namespace = "com.example.movie.database"
}

dependencies {
    implementation(libs.koin.android.workmanager)
    implementation(projects.core.data)
    implementation(projects.core.domain)
}