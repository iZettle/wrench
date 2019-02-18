import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.oss.licenses.plugin")
}

// https://github.com/gradle/kotlin-dsl/issues/644#issuecomment-398502551
// androidExtensions { isExperimental = true }
androidExtensions {
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

kapt {
    javacOptions {
        // Increase the max count of errors from annotation processors.
        // Default is 100.
        option("-Xmaxerrs", 500)
    }
}

android {
    testOptions {
        unitTests.setIncludeAndroidResources(true)
    }

    dataBinding {
        isEnabled = true
    }

    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        applicationId = "com.izettle.wrench"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.appVersionCode
        versionName = Versions.appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }

        val configurationAuthorityValue = "$applicationId.configprovider"
        val wrenchPermission = "$applicationId.permission"

        manifestPlaceholders = mapOf("configurationAuthority" to configurationAuthorityValue, "wrenchPermission" to wrenchPermission)

        buildConfigField("String", "CONFIG_AUTHORITY", "\"$configurationAuthorityValue\"")

    }
    packagingOptions {
        exclude("META-INF/main.kotlin_module")
        exclude("mockito-extensions/org.mockito.plugins.MockMaker")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro", "proguard-stetho.pro")
        }
        getByName("debug") {
            versionNameSuffix = " debug"
            applicationIdSuffix = ".debug"
        }
    }
    lintOptions {
        baselineFile = file("lint-baseline.xml")
        isCheckReleaseBuilds = true
        isAbortOnError = true
        isWarningsAsErrors = true
        setLintConfig(File("../lint.xml"))
    }
    sourceSets {
        // debug.assets.srcDirs => https://github.com/robolectric/robolectric/issues/3928
        // debug.assets.srcDirs += files("$projectDir/schemas".toString())
        getByName("androidTest") {
            assets.srcDirs(files("$projectDir/schemas"))
        }
    }
    useLibrary("android.test.runner")
    useLibrary("android.test.base")
    useLibrary("android.test.mock")
}

dependencies {
    testImplementation(Libs.junit)
    testImplementation(Libs.mockito.core)

    testImplementation(Libs.androidTestingSupportLibrary.core)
    testImplementation(Libs.androidTestingSupportLibrary.truth)
    testImplementation(Libs.androidTestingSupportLibrary.rules)
    testImplementation(Libs.androidTestingSupportLibrary.runner)
    testImplementation(Libs.androidTestingSupportLibrary.junit)
    testImplementation(Libs.mockito.android)
    testImplementation(Libs.wrench.prefs)
    testImplementation(Libs.room.testing)
    testImplementation(Libs.koin.koinTest)
    testImplementation(Libs.robolectric)

    androidTestImplementation(Libs.androidTestingSupportLibrary.core)
    androidTestImplementation(Libs.androidTestingSupportLibrary.truth)
    androidTestImplementation(Libs.androidTestingSupportLibrary.rules)
    androidTestImplementation(Libs.androidTestingSupportLibrary.runner)
    androidTestImplementation(Libs.androidTestingSupportLibrary.junit)
    androidTestImplementation(Libs.mockito.android)
    androidTestImplementation(Libs.wrench.prefs)
    androidTestImplementation(Libs.room.testing)

    kapt(Libs.lifecycle.compiler)
    kapt(Libs.room.compiler)

    implementation(Libs.support.app_compat)
    implementation(Libs.support.recyclerview)
    implementation(Libs.support.design)
    implementation(Libs.stetho) {
        exclude(group = "com.google.code.findbugs", module = "jsr305")
    }
    implementation(Libs.constraint_layout)
    implementation(Libs.support.cardview)
    implementation(Libs.lifecycle.extensions)
    implementation(Libs.room.runtime)
    implementation(Libs.paging)

    implementation(Libs.navigation.fragmentKotlin)
    implementation(Libs.navigation.uiKotlin)

    implementation(Libs.wrench.core)
    implementation(Libs.wrench.prefs)
    implementation(Libs.kotlin.stdlib)
    implementation(Libs.kotlin.coroutinesAndroid)

    implementation(Libs.koin.androidx)

}
