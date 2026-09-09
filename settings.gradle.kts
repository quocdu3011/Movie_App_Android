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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieApp"

include(
    ":app",
    ":core-ui",
    ":core-network",
    ":core-database",
    ":core-player",
    ":core-common",
    ":domain",
    ":data",
    ":feature-auth",
    ":feature-home",
    ":feature-detail",
    ":feature-player",
    ":feature-search",
    ":feature-profile",
    ":feature-subscription",
)
