plugins {
    alias(libs.plugins.mymovies.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paging.compose)
    implementation(projects.core.domain)
}