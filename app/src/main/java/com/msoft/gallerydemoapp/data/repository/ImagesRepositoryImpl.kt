package com.msoft.gallerydemoapp.data.repository

import android.app.Application
import android.app.PendingIntent
import android.net.Uri
import android.os.Build
import android.text.format.Formatter.formatShortFileSize
import androidx.annotation.RequiresApi
import com.msoft.gallerydemoapp.data.datasource.LocalImagesDataSource
import com.msoft.gallerydemoapp.data.models.ImageInfo
import com.msoft.gallerydemoapp.data.resource.Resource
import com.msoft.gallerydemoapp.domain.use_case.FormatDateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ImagesRepositoryImpl(
    private val app: Application,
    private val localImagesDataSource: LocalImagesDataSource,
    private val formatDateUseCase: FormatDateUseCase
) : ImagesRepository {

    private val refreshImagesScope = CoroutineScope(Dispatchers.IO)

    override val allImages: StateFlow<Resource<List<Uri>>> = MutableStateFlow(Resource.Data(emptyList()))

    override fun getImageInfo(uri: String): ImageInfo {
        with(localImagesDataSource.getImageMetadata(Uri.parse(uri))) {
            return ImageInfo(name, formatDateUseCase.invoke(Date(dateTaken)), formatShortFileSize(app, size))
        }
    }

    override fun refreshImages() {
        refreshImagesScope.launch {
            (allImages as MutableStateFlow).update { Resource.Loading() }
            val images = localImagesDataSource.getAllImages()
            allImages.update { Resource.Data(images) }
        }
    }

    override fun deleteImage(uri: String) = localImagesDataSource.deleteImage(Uri.parse(uri))

    @RequiresApi(Build.VERSION_CODES.R)
    override fun getDeleteImageIntent(uri: String): PendingIntent = localImagesDataSource.getDeleteImagesIntent(listOf(Uri.parse(uri)))

}
