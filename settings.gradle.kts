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

rootProject.name = "AndroidLab Kotlin"
include(":app")
include(":ch01")
include(":ch02")
include(":ch03")
include(":ch03_outer")
include(":ch4")
include(":ch5")
include(":ch5_outer")
include(":ch6")
include(":ch6_outer")
include(":ch7")
include(":ch8")
