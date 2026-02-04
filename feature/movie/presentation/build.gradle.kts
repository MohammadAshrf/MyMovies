plugins {
    alias(libs.plugins.mymovies.android.feature.ui)
}

android {
    namespace = "com.example.movie.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.bundles.unit.testing)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.feature.movie.domain)
}