plugins {
    alias(libs.plugins.mymovies.android.library)
    alias(libs.plugins.mymovies.jvm.ktor)
    alias(libs.plugins.mymovies.android.room)
}

android {
    namespace = "com.example.movie.data"
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.bundles.unit.testing)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.feature.movie.domain)
    implementation(projects.feature.movie.database)
}