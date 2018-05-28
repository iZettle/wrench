// https://handstandsam.com/2018/02/11/kotlin-buildsrc-for-better-gradle-dependency-management/

object Versions {
    val targetSdk = 27
    val compileSdk = 27
    val minSdk = 14

    val appVersionCode = 11
    val appVersionName = "1.0.10"

    val dagger = "2.16"

    val support_lib = "27.1.1"
    val constraint_layout = "1.1.0"

    val wrench = "0.3"

    val arch_core = "1.1.1"
    val room = "1.1.0"
    val lifecycle = "1.1.1"
    val support = "27.1.1"
    val junit = "4.12"
    val espresso = "3.0.2"
    val retrofit = "2.3.0"
    val okhttp_logging_interceptor = "3.9.0"
    val mockwebserver = "3.8.1"
    val apache_commons = "2.5"
    val mockito = "2.18.3"
    val mockito_all = "1.10.19"
    val dexmaker = "2.2.0"
    val glide = "4.7.1"
    val timber = "4.7.0"
    val android_gradle_plugin = "3.2.0-alpha15"
    val rxjava2 = "2.1.3"
    val rx_android = "2.0.1"
    val atsl_runner = "1.0.2"
    val atsl_rules = "1.0.2"
    val hamcrest = "1.3"
    val kotlin = "1.2.41"
    val paging = "1.0.0"
    val work = "1.0.0-alpha01"
    val navigation = "1.0.0-alpha01"
}

object AndroidTestingSupportLibrary {
    val runner = "com.android.support.test:runner:${Versions.atsl_runner}"
    val rules = "com.android.support.test:rules:${Versions.atsl_rules}"
}

object Lifecycle {
    val core = "android.arch.lifecycle:livedata-core:${Versions.lifecycle}"
    val runtime = "android.arch.lifecycle:runtime:${Versions.lifecycle}"
    val extensions = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
    val java8 = "android.arch.lifecycle:common-java8:${Versions.lifecycle}"
    val compiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"
}

object Espresso {
    val core = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
    val contrib = "com.android.support.test.espresso:espresso-contrib:${Versions.espresso}"
    val intents = "com.android.support.test.espresso:espresso-intents:${Versions.espresso}"
}

object Room {
    val runtime = "android.arch.persistence.room:runtime:${Versions.room}"
    val compiler = "android.arch.persistence.room:compiler:${Versions.room}"
    val rxjava2 = "android.arch.persistence.room:rxjava2:${Versions.room}"
    val testing = "android.arch.persistence.room:testing:${Versions.room}"
}

object Mockito {
    val core = "org.mockito:mockito-core:${Versions.mockito}"
    val all = "org.mockito:mockito-all:${Versions.mockito_all}"
}

object Kotlin {
    val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val allopen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
}

object Wrench {
    val core = "com.izettle.wrench:wrench-core:${Versions.wrench}"
    val prefs = "com.izettle.wrench:wrench-prefs:${Versions.wrench}"
    val prefs_no_op = "com.izettle.wrench:wrench-prefs-no-op:${Versions.wrench}"
}

object Navigation {

    val fragment = "android.arch.navigation:navigation-fragment:${Versions.navigation}"
    val ui = "android.arch.navigation:navigation-ui:${Versions.navigation}"

    val fragmentKotlin = "android.arch.navigation:navigation-fragment-ktx:${Versions.navigation}"
    val uiKotlin = "android.arch.navigation:navigation-ui-ktx:${Versions.navigation}"

    val testing = "android.arch.navigation:navigation-testing:${Versions.navigation}"

    val safeArgsPlugin = "android.arch.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
}

object Oss {
    val plugin = "com.google.gms:oss-licenses:0.9.2"
    val runtime = "com.google.android.gms:play-services-oss-licenses:15.0.1"
}

object Libs {
    val wrench = Wrench
    val lifecycle = Lifecycle
    val support = Support
    val dagger = Dagger
    val mockito = Mockito
    val androidTestingSupportLibrary = AndroidTestingSupportLibrary
    val espresso = Espresso
    val room = Room
    val kotlin = Kotlin
    val navigation = Navigation
    val oss = Oss

    val paging = "android.arch.paging:runtime:${Versions.paging}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"

    val constraint_layout = "com.android.support.constraint:constraint-layout:${Versions.constraint_layout}"

    val junit = "junit:junit:${Versions.junit}"

}

object Dagger {
    val runtime = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
}

object Support {
    val annotations = "com.android.support:support-annotations:${Versions.support}"
    val app_compat = "com.android.support:appcompat-v7:${Versions.support}"
    val recyclerview = "com.android.support:recyclerview-v7:${Versions.support}"
    val cardview = "com.android.support:cardview-v7:${Versions.support}"
    val design = "com.android.support:design:${Versions.support}"
    val v4 = "com.android.support:support-v4:${Versions.support}"
    val core_utils = "com.android.support:support-core-utils:${Versions.support}"
}
