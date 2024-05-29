package com.msoft.gallerydemoapp.ui.vm

import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.msoft.gallerydemoapp.data.models.ImageData
import com.msoft.gallerydemoapp.data.models.UiState
import com.msoft.gallerydemoapp.data.repository.ImagesRepository
import com.msoft.gallerydemoapp.domain.use_case.GetImagesWithFavouritesInfoUseCase
import com.msoft.gallerydemoapp.utils.DELETE_IMAGES_PERMISSION
import com.msoft.gallerydemoapp.utils.READ_IMAGES_PERMISSIONS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ImagesListViewModel @Inject constructor(
    private val app: Application,
    private val imagesRepository: ImagesRepository,
    getImagesWithFavouritesInfoUseCase: GetImagesWithFavouritesInfoUseCase
) : AndroidViewModel(app) {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.LOADING)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _imagesData: MutableStateFlow<List<ImageData>> = MutableStateFlow(emptyList())
    val imagesData: StateFlow<List<ImageData>> = _imagesData.asStateFlow()

    private val showImagesPermissionsGranted: MutableStateFlow<Boolean> =
        MutableStateFlow(READ_IMAGES_PERMISSIONS.all { ContextCompat.checkSelfPermission(app, it) == PERMISSION_GRANTED })

    private val _deleteImagesPermissionGranted: MutableStateFlow<Boolean> =
        MutableStateFlow(
            if (DELETE_IMAGES_PERMISSION != null) ContextCompat.checkSelfPermission(app, DELETE_IMAGES_PERMISSION!!) == PERMISSION_GRANTED else true
        )
    val deleteImagesPermissionGranted: StateFlow<Boolean> = _deleteImagesPermissionGranted.asStateFlow()

    init {
        reloadImages()

        combine(imagesData.debounce(1000), showImagesPermissionsGranted) { uris, granted ->
            _uiState.update {
                when {
                    granted.not() -> UiState.PERMISSIONS_NOT_GRANTED
                    uris.isEmpty() -> UiState.NO_IMAGES
                    else -> UiState.CONTENT
                }
            }
        }.launchIn(viewModelScope)

        getImagesWithFavouritesInfoUseCase.invoke().onEach {
            _imagesData.emit(it)
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
