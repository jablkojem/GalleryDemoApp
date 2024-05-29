package com.msoft.gallerydemoapp.domain.use_case

import com.msoft.gallerydemoapp.data.models.ImageData
import com.msoft.gallerydemoapp.data.repository.FavouriteImagesRepository
import com.msoft.gallerydemoapp.data.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetImagesWithFavouritesInfoUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
    private val favouriteImagesRepository: FavouriteImagesRepository
) {
    operator fun invoke(): Flow<List<ImageData>> =
        combine(imagesRepository.allImages, favouriteImagesRepository.favouriteImages) { images, favouritesList ->
            images.map { ImageData(it, favouritesList.contains(it.toString())) }
        }
}
