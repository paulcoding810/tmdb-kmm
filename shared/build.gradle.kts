import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
            api(libs.koin.android)
            api(libs.koin.compose)
            api(libs.koin.compose.multiplatform)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.room.paging)
        }
        iosMain.dependencies { implementation(libs.sqldelight.native) }
        commonMain.dependencies {

            //put your multiplatform dependencies here
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.skie.annotations)
            api(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.kotlinx.atomicfu)
            api(libs.androidx.datastore.preferences.core)
            api(libs.androidx.datastore.core.okio)
            implementation(libs.okio)
            implementation(libs.kermit)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.multiplatform)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.kotlinx.serialization)
            implementation(libs.napier)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.paulcoding.tmdb"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

sqldelight {
    databases {
        create("TMDBDatabase") {
            packageName = "com.paulcoding.tmdb.db"
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/com/paulcoding/tmdb/schemas"))
            migrationOutputDirectory.set(file("src/commonMain/sqldelight/com/paulcoding/tmdb/migrations"))
            verifyMigrations = true
        }
    }
}