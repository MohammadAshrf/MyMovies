pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyMovies"
include(":app")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:domain")
include(":core:data")
include(":feature:movie:presentation")
include(":feature:movie:domain")
include(":feature:movie:data")
include(":feature:movie:database")
