package com.msoft.gallerydemoapp.data.datasource

import android.app.Application
import android.app.PendingIntent
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.msoft.gallerydemoapp.data.models.ImageMetadata

class LocalImagesDataSource(private val app: Application) {
    fun getAllImages(): List<Uri> {
        val galleryImageUrls = mutableListOf<Uri>()
        val columns = arrayOf(MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        app.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, "$orderBy DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                galleryImageUrls.add(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id))
            }
        }

        return galleryImageUrls
    }

    fun deleteImage(uri: Uri) {
        app.contentResolver.delete(uri, null, null)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getDeleteImagesIntent(uris: List<Uri>): PendingIntent {
        return MediaStore.createDeleteRequest(app.contentResolver, uris)
    }

    fun getImageMetadata(uri: Uri): ImageMetadata {
        var data = ImageMetadata("", 0L, 0L)

        val projection =
            arrayOf(MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE)
        app.contentResolver.query(uri, projection, null, null, null)?.let { cursor ->
            try {
                if (cursor.moveToFirst()) {
                    data = ImageMetadata(
                        cursor.getString(0),
                        cursor.getLong(1),
                        cursor.getLong(2)
                    )
                }
            } finally {
                cursor.close()
            }
        }

        return data
    }
}
