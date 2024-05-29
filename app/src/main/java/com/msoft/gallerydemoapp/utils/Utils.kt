package com.msoft.gallerydemoapp.utils

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.res.Configuration
import android.os.Build
import androidx.compose.ui.tooling.preview.Preview

val READ_IMAGES_PERMISSIONS = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> listOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> listOf(READ_MEDIA_IMAGES)
    else -> listOf(READ_EXTERNAL_STORAGE)
}

var DELETE_IMAGES_PERMISSION = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> null
    else -> WRITE_EXTERNAL_STORAGE
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DefaultPreviews

const val FAVOURITE_IMAGES_DB_NAME = "FAVOURITE_IMAGES_DB"

const val HOURS_24_FORMAT_LONG = "d. M. YYYY, H:mm"
