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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class ImagesRepositoryImpl(
    private val app: Application,
    private val localImagesDataSource: LocalImagesDataSource,
    private val formatDateUseCase: FormatDateUseCase
) : ImagesRepository {

    private val refreshImagesTrigger = MutableSharedFlow<Unit>(1)

    override val allImages: Flow<Resource<List<Uri>>> = MutableStateFlow<Resource<List<Uri>>>(Resource.Data(emptyList())).apply {
        CoroutineScope(Dispatchers.IO).launch {
            refreshImagesTrigger.collect {
                value = Resource.Loading()
                val images = localImagesDataSource.getAllImages()
                value = Resource.Data(images)
            }
        }
    }

    override fun getImageInfo(uri: String): ImageInfo {
        with(localImagesDataSource.getImageMetadata(Uri.parse(uri))) {
            return ImageInfo(name, formatDateUseCase.invoke(Date(dateTaken)), formatShortFileSize(app, size))
        }
    }

    override fun refreshImages() {
        refreshImagesTrigger.tryEmit(Unit)
    }

    override fun deleteImage(uri: String) = localImagesDataSource.deleteImage(Uri.parse(uri))

    @RequiresApi(Build.VERSION_CODES.R)
    override fun getDeleteImageIntent(uri: String): PendingIntent = localImagesDataSource.getDeleteImagesIntent(listOf(Uri.parse(uri)))

}
