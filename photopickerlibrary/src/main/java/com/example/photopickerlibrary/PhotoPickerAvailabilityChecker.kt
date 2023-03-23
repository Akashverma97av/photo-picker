package com.example.photopickerlibrary

import android.os.Build
import android.os.ext.SdkExtensions.getExtensionVersion
import androidx.annotation.RequiresApi

private const val ANDROID_R_REQUIRED_EXTENSION_VERSION = 2

object PhotoPickerAvailabilityChecker {
    @RequiresApi(33)
            /*This method is used to check the version of device*/
    fun isPhotoPickerAvailable(): Boolean {
        return when {
            /*return true of Android 13*/
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> true
            /*return true for Android 12 and 11 except Android 11,12 go devices*/
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                getExtensionVersion(Build.VERSION_CODES.R) >= ANDROID_R_REQUIRED_EXTENSION_VERSION
            }
            else -> false
        }

    }

}