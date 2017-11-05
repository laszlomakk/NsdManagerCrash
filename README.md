# NsdManagerCrash
Infinite bootloop on modern Android from user-space.

## Intro

This repository is a proof of concept Android application for crashing a phone.

Currently there are four branches (master + three others) of the repo,
each showcasing a different (but similar) way to force a soft-reboot of the device.

If you force the soft-reboot after the device completes the boot, you can force the device to enter a boot loop.
The master branch illustrates how this can be done.

## Surely this does not work on my phone !?

It works on modern Android, starting around KitKat (Android 4.4, API 19), up to at least Oreo (Android 8.0, API 26).

Last tested on a real Nexus 5X, running Android 8.0.0 with 5 Oct 2017 security patches. (build number: OPR4.170623.009)

This has been reported on the public Google Issue Tracker on July 4, 2016: https://issuetracker.google.com/issues/37108914

## How it works

It exploits the lack of null checks in the NsdManager API (part of Android).
Unsurprisingly this lack of checks is in private methods, not the public API, but hey that's what Java Reflection is for!

The resulting `NullPointerException`s will crash NsdService, which runs in the system process, and that gets `SIGKILL`ed.

## Permissions

To use the NsdManager API, the prototype app needs `android.permission.INTERNET`.

To start on boot, and hence enter a boot-loop, the app needs `android.permission.RECEIVE_BOOT_COMPLETED`.

No other permissions are needed.

## Build Instructions

The standard Android SDK is insufficient to build this code.
You will need the hidden/internal API too, see https://github.com/anggrayudi/android-hidden-api

