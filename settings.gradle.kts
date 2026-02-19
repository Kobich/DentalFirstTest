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

rootProject.name = "DentalFirst"
include(":app")
include(":feature:cart:api")
include(":feature:cart:impl")
include(":base:ui")
include(":ui:cart:api")
include(":ui:cart:impl")
include(":ui:catalog:api")
include(":ui:catalog:impl")
 
