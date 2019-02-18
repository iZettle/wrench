plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(Versions.compileSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val wrenchProviderAuthority = "com.izettle.wrench.configprovider"

        manifestPlaceholders = mapOf("wrenchProviderAuthority" to wrenchProviderAuthority)
        buildConfigField("String", "WRENCH_AUTHORITY", "\"${wrenchProviderAuthority}\"")

        buildConfigField("int", "WRENCH_API_VERSION", "1")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
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
    implementation(Libs.support.annotations)
}


apply(rootProject.file("gradle/gradle-mvn-push.gradle"))