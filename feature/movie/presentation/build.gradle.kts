plugins {
    alias(libs.plugins.mymovies.android.feature.ui)
}

android {
    namespace = "com.example.movie.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.feature.movie.domain)
}