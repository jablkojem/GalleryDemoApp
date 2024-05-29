package com.msoft.gallerydemoapp.ui.vm

import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.msoft.gallerydemoapp.data.models.ImageData
import com.msoft.gallerydemoapp.data.models.UiState
import com.msoft.gallerydemoapp.data.repository.ImagesRepository
import com.msoft.gallerydemoapp.data.resource.Resource
import com.msoft.gallerydemoapp.domain.use_case.GetImagesWithFavouritesInfoUseCase
import com.msoft.gallerydemoapp.utils.DELETE_IMAGES_PERMISSION
import com.msoft.gallerydemoapp.utils.READ_IMAGES_PERMISSIONS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImagesListViewModel @Inject constructor(
    private val app: Application,
    private val imagesRepository: ImagesRepository,
    getImagesWithFavouritesInfoUseCase: GetImagesWithFavouritesInfoUseCase
) : AndroidViewModel(app) {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val imagesDataResource: MutableStateFlow<Resource<List<ImageData>>> = MutableStateFlow(Resource.Data(emptyList()))
    val displayedImages: StateFlow<List<ImageData>> =
        imagesDataResource.filter { it is Resource.Data }.map { (it as Resource.Data).data }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val showImagesPermissionsGranted: MutableStateFlow<Boolean> =
        MutableStateFlow(READ_IMAGES_PERMISSIONS.all { ContextCompat.checkSelfPermission(app, it) == PERMISSION_GRANTED })

    private val _deleteImagesPermissionGranted: MutableStateFlow<Boolean> =
        MutableStateFlow(
            if (DELETE_IMAGES_PERMISSION != null) ContextCompat.checkSelfPermission(app, DELETE_IMAGES_PERMISSION!!) == PERMISSION_GRANTED else true
        )
    val deleteImagesPermissionGranted: StateFlow<Boolean> = _deleteImagesPermissionGranted.asStateFlow()

    init {
        reloadImages()

        combine(
            displayedImages.map { it.isNotEmpty() },
            imagesDataResource,
            showImagesPermissionsGranted
        ) { hasAlreadyVisibleImages, imagesResource, permissionsGranted ->
            _uiState.update {
                when {
                    permissionsGranted.not() -> UiState.PERMISSIONS_NOT_GRANTED
                    hasAlreadyVisibleImages.not() && imagesResource is Resource.Loading -> UiState.LOADING
                    imagesResource is Resource.Data && imagesResource.data.isEmpty() -> UiState.NO_IMAGES
                    else -> UiState.CONTENT
                }
            }
        }.launchIn(viewModelScope)

        getImagesWithFavouritesInfoUseCase.invoke().onEach {
            imagesDataResource.emit(it)
        }.launchIn(viewModelScope)
    }

    fun reloadImages() {
        if (showImagesPermissionsGranted.value) imagesRepository.refreshImages()
    }

    fun onShowImagesPermissionsGranted() {
        reloadImages()
        showImagesPermissionsGranted.update { true }
    }

    fun onDeleteImagesPermissionsGranted() {
        _deleteImagesPermissionGranted.update { true }
    }
}
