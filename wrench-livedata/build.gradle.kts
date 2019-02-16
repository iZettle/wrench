plugins {
    id("com.android.library")
}
android {
    compileSdkVersion(Versions.compileSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    lintOptions {
        baselineFile = file("lint-baseline.xml")
        isCheckReleaseBuilds = true
        isAbortOnError = true
        isWarningsAsErrors = true
        setLintConfig(File("../lint.xml"))
    }
}

dependencies {
    testImplementation(Libs.junit)
//    implementation Libs.wrench.core
    implementation(Libs.wrench.core)
    implementation(Libs.support.annotations)
    api(Libs.lifecycle.core)
}

// The api of this module should be discussed before any potential release
// apply from: rootProject.file('gradle/gradle-mvn-push.gradle')