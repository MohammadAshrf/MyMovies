plugins {
    alias(libs.plugins.mymovies.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.domain)
}