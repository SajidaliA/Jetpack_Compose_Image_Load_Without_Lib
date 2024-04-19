# Sajid_Ali_Android_Assignment

Android assignment project for showing images in a grid with 3-column square images without any image loading library like Glide, Coil, Picaso, etc. 
Built on Android Jetpack compose - MVVM with Hilt for DI, Room for local disk cache, and LRU cache for memory cache

## Table of Contents

- [Introduction](#introduction)
- [Detail Description](#detaildescription)
- [Installation](#installation)



## Introduction

Welcome to Sajid_Ali_Android_Assignment, an Android application designed to showcase a 3-column grid layout with square images. 
This project is built on :

1. Android studio Android Studio Iguana | 2023.2.1 Patch 2
2. Kotlin DSL (Domain-Specific Languages)
3. KSP (Kotlin Symbol Processing API)
4. Version catalog system (In build.gradle (Both)
5. Android Jetpack compose
6. MVVM architecture design pattern
7. Hilt for Dependency injection
8. Room database for local disk cache
9. LRU cache for memory cache
10. Retrofit for API call

- After installing the app only the first time internet is required to get images from the server.
- Later the App can load images from the cache if no internet is available.
- When the internet is available it will update the caches

## Detail descriptions 

1. Hilt for Dependency Injection (DI): Hilt, Google's recommended library for Android dependency injection, ensures a modular and maintainable codebase.
   With Hilt, managing dependencies becomes seamless and intuitive, allowing for easier testing and scaling of the app.
   
2. Room for Disk Cache: This project integrates Room, a powerful persistence library provided by the Android Jetpack, to implement disk caching.
   This ensures efficient storage and retrieval of data, enhancing app performance and responsiveness even in offline scenarios.
   
3. LRU Cache for Memory Management: LRU (Least Recently Used) cache for efficient memory management,
   optimizing resource utilization and enhancing the app's overall speed and responsiveness.
   
5. Retrofit for API Calls: With Retrofit, a type-safe HTTP client for Android and Java, This app seamlessly integrates with external APIs,
   facilitating smooth and reliable data exchange between the app and remote servers.
   Enjoy fast and reliable network requests, ensuring a seamless user experience.

## Installation

Here are the steps to installation.

1. Clone this repository:
$ git clone https://github.com/SajidaliA/Sajid_Ali_Android_Assignment.git
2. Open the project in Android Studio Iguana | 2023.2.1 Patch 2
3. Build and run the project on an emulator or a physical device.


