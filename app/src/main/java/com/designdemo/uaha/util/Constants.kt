@file:Suppress("NewLineAtEndOfFile")
@file:JvmName("Constants")
package com.designdemo.uaha.util

// Notification Channel constants
@JvmField val NOTIFICATION_CHANNEL_NAME: CharSequence = "Historian Notifications"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications with info about OS or Devices"
@JvmField val NOTIFICATION_TITLE: CharSequence = "Historian Notification"
const val CHANNEL_ID = "HISTORIAN_NOTIFICATION"
const val NOTIFICATION_ID = 1
const val KEY_NOTIF_LASTDATE = "WORKRESP_LASTDATE"
const val TAG_WORK_NOTIF = "TagWorkNotif"

// Validation Constants
const val PASSWORD_MIN = 8
const val NAME_MIN = 4
const val NAME_MAX = 10
const val PHONE_LENGTH = 14

// Bottom Sheet Constants
const val PEEK_HEIGHT_PIXEL = 300
const val ROTATION_180 = -180