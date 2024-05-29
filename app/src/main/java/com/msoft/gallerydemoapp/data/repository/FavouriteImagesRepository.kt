package com.msoft.gallerydemoapp.data.repository

import kotlinx.coroutines.flow.Flow

interface FavouriteImagesRepository {
    val favouriteImages: Flow<List<String>>

    fun addToFavourites(uri: String)
    fun removeFromFavourites(uri: String)
}
