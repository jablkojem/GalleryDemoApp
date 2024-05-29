package com.msoft.gallerydemoapp.data.repository

import android.app.PendingIntent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.msoft.gallerydemoapp.data.models.ImageInfo
import kotlinx.coroutines.flow.StateFlow

interface ImagesRepository {
    val allImages: StateFlow<List<Uri>>
    fun getImageInfo(uri: String): ImageInfo
    fun refreshImages()

    // android 10 and older
    fun deleteImage(uri: String)

    // android 11+ needs to open intent with delete request
    @RequiresApi(Build.VERSION_CODES.R)
    fun getDeleteImageIntent(uri: String): PendingIntent
}
