pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven( "https://jitpack.io")
    }
}

rootProject.name = "QuickScoreboardCreator"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:di")
include(":core:domain")
include(":core:database")
include(":core:data")
include(":core:util")

include(":feature:sport:presentation")
include(":feature:sport:di")
include(":feature:sport:domain")
include(":feature:sport:data")

include(":feature:team:presentation")
include(":feature:team:di")
include(":feature:team:domain")
include(":feature:team:data")

include(":feature:history:di")
include(":feature:history:domain")
include(":feature:history:data")

include(":feature:scoreboard:di")
include(":feature:scoreboard:domain")
include(":feature:history:presentation")
include(":feature:scoreboard:presentation")
