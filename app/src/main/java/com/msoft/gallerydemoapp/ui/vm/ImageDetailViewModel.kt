package com.msoft.gallerydemoapp.ui.vm

import android.app.Application
import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.msoft.gallerydemoapp.data.repository.FavouriteImagesRepository
import com.msoft.gallerydemoapp.data.repository.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val app: Application,
    private val imagesRepository: ImagesRepository,
    private val favouriteImagesRepository: FavouriteImagesRepository
) : AndroidViewModel(app) {

    private val _currentUri: MutableStateFlow<String> = MutableStateFlow("")
    val currentUri: StateFlow<String> = _currentUri.asStateFlow()

    val isFavourite: StateFlow<Boolean> = combine(favouriteImagesRepository.favouriteImages, currentUri) { allFavourites, current ->
        allFavourites.contains(current)
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun switchFavouriteButtonClick(uri: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isFavourite.value) favouriteImagesRepository.removeFromFavourites(uri)
            else favouriteImagesRepository.addToFavourites(uri)
        }
    }

    fun setCurrentUri(uri: String) {
        _currentUri.update { uri }
    }

    fun deleteImage(uri: String) {
        favouriteImagesRepository.removeFromFavourites(uri)
        imagesRepository.deleteImage(uri)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun getDeleteImageIntent(uri: String): PendingIntent = imagesRepository.getDeleteImageIntent(uri)

    fun getImageInfo(uri: String) = imagesRepository.getImageInfo(uri)
}
