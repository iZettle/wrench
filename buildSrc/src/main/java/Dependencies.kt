// https://handstandsam.com/2018/02/11/kotlin-buildsrc-for-better-gradle-dependency-management/

object Versions {
    val targetSdk = 27
    val compileSdk = 27
    val minSdk = 14

    val support_lib = "27.1.1"
    val wrench = "0.3"
    val dagger = "2.16"
}

object Libs {
    val support_annotations = "com.android.support:support-annotations:${Versions.support_lib}"
    val support_appcompat_v7 = "com.android.support:appcompat-v7:${Versions.support_lib}"
    val support_design = "com.android.support:design:${Versions.support_lib}"
    val support_customtabs = "com.android.support:customtabs:${Versions.support_lib}"
    val support_card = "com.android.support:cardview-v7:${Versions.support_lib}"

    val timber = "com.jakewharton.timber:timber:4.7.0"

    val wrench_core = "com.izettle.wrench:wrench-core:${Versions.wrench}"
    val wrench_prefs = "com.izettle.wrench:wrench-prefs:${Versions.wrench}"
    val wrench_prefs_no_op = "com.izettle.wrench:wrench-prefs-no-op:${Versions.wrench}"

    val constraint_layout = "com.android.support.constraint:constraint-layout:1.1.0"

    val junit = "junit:junit:4.12"

    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"


}