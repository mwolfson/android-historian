Android Historian - Material Design Demo
========================================

This app is designed to demonstrate the various components of the Material Design Components, in particular, showing how to implement Material design into your app, using these controls.

The UI is has specific considerations to demonstrate certain interactions of the components, so may have certain unusual interactions (and patterns).

The [official Material site](https://material.io/develop/android/) has great information specific to Android Development.

Current Build Status Is:
[![Build Status](https://app.bitrise.io/app/185d24999f31db04/status.svg?token=H8R81uEqmKABt_zQjgNRog&branch=master)](https://app.bitrise.io/app/185d24999f31db04)

_Modifiy gradle.properties to build locally_

In order to use some the API functionality in code, you will need add your own API key to `gradle.properties`
[Fono API](https://fonoapi.freshpixl.com/)

Now you can download this app from the Play Store (You must [join The Beta Distribution Channel](https://play.google.com/apps/testing/com.ableandroid.historian) )

![Screen Flow Demo](https://github.com/mwolfson/android-historian/blob/master/art/historian2.gif)

Apr 6, 2019
- Adaptive Icon, w/ Custom Vector Logo
- WorkManager Example (sending periodic notifications)

Feb 2, 2019
"Modern Android" Update
- 100% Kotlin 
- Converted to MVVM
    - LiveData
- Room Database
- Coroutines
- Jetpack Dependencies

Oct 16, 2018
- Added API for getting Phone information

July 18, 2018
- Bottom App Bar (with inset FAB)
- Chip
- Text Scale Attributes
- Bottom Sheet
    - Coordinated Motion
- MDC style TextInputView with Outline

May 24, 2018
- Update packages and build
    - Jetpack conversion (androidx)
    - Using new Material Component Packages
- Auto-Resizing TextViews

Apr 10, 2016
- Bottom Sheets
- Vector Drawables
- SharedElementTransition

Oct 22, 2015
- PercentageLayout

Sept 15, 2015
- AppCompatActivity
- CoordinatorLayout
- AppBarLayout & Toolbar
- RecyclerView (with ItemDecorators)
    - LinearLayoutManager
    - GridLayoutManager
    - StaggeredGridLayoutManager        
- Snackbar
- TabLayout
- AppCompat Tinting
- NavigationView
- Snackbar
- SwitchCompat
- AlertDialog
- CardView
- FloatingActionButton
- TextInputLayout
- TextAppearance.AppCompat

Special Thanks
--------------

Item Decorator from [Stacy Devino](https://twitter.com/doesitpew/)

Pre-requisites
--------------

- Android SDK, Build Tools and Support Repository

Presentation Slides and Video
-----------------------------

This project was originally created to support my presentation titled "Material Design Everywhere using the Android Support Libs".

- My most current slides from this presentation are posted here: http://tiny.cc/MaterialAppCompat
- The video from Droidcon NYC: https://www.youtube.com/watch?v=5u0dtzXL3PQ


