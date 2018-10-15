# Wrench - feature switching

Store app settings / feature toggles in an external application making in persist across clean data / reinstalls.

## Wrench app

Download on [play store](https://play.google.com/store/apps/details?id=com.izettle.wrench).

Stores the settings / toggles behind a content provider.

## Wrench-prefs
Wrapper library with a SharedPreference like api to get and set configurations using wrench provider.
```
dependencies {
    implementation 'com.izettle.wrench:wrench-prefs:0.3'
    // ....
}
```
*app build.gradle*

```
WrenchPreferences wrenchPreferences = new WrenchPreferences(getApplicationContext());
boolean myBetaFeature = wrenchPreferences.getBoolean("My unreleased feature", false)

if (myBetaFeature) {
    performBetaBehaviour();
} else {
    performRegularBehaviour();
}
```
*MyActivity.java*

## Wrench-prefs-no-op
No-op version of WrenchPreferences that can be used in a release build:
```
dependencies {
    debugImplementation 'com.izettle.wrench:wrench-prefs:0.3'
    releaseImplementation 'com.izettle.wrench:wrench-prefs-no-op:0.3'
    // ....
}
```
*app build.gradle*

## Wrench-core
Contains building blocks to talk to the wrench provider. Only needed when
implementing a library to expose the data in the provider.

##### WrenchProviderContract:
Helper providing URIs for the wrench provider.

##### Bolt:
The object returned by the provider when queried. Contains type, key and value of the setting.

##### Nut:
Additional information about the bolt that can be provided. Used for implementing dropdowns for enums.

## Extras
Check build on [Travis](https://travis-ci.org/iZettle/wrench/)

Build on develop currently ![Build status](https://travis-ci.org/iZettle/wrench.svg?branch=develop)

Build on master currently ![Build status](https://travis-ci.org/iZettle/wrench.svg?branch=master)

This is a development tools meant to facilitate feature switching in an external app so that configurations
will be retained across clear data / uninstalls.

It is based on a ContentProvider.

