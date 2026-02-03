package com.example.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        val apiKey = gradleLocalProperties(rootDir, rootProject.providers).getProperty("API_KEY")
        val apiAccessToken = gradleLocalProperties(rootDir, rootProject.providers).getProperty("API_ACCESS_TOKEN")
        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey, apiAccessToken)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, apiKey, apiAccessToken)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(apiKey, apiAccessToken)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, apiKey, apiAccessToken)
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(apiKey: String, apiAccessToken: String) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "API_ACCESS_TOKEN", "\"$apiAccessToken\"")
    buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3\"")
    buildConfigField("String", "IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/w500/\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    apiKey: String,
    apiAccessToken: String
) {
    buildConfigField("String", "API_KEY", "\"$apiKey\"")
    buildConfigField("String", "API_ACCESS_TOKEN", "\"$apiAccessToken\"")
    buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3\"")
    buildConfigField("String", "IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/w500/\"")

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}