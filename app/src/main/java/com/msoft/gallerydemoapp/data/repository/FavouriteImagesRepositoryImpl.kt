package com.msoft.gallerydemoapp.data.repository

import com.msoft.gallerydemoapp.data.datasource.dao.FavouriteImagesDao
import com.msoft.gallerydemoapp.data.models.db.Image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteImagesRepositoryImpl @Inject constructor(
    private val favouriteImagesDao: FavouriteImagesDao
) : FavouriteImagesRepository {

    override val favouriteImages: Flow<List<String>> = favouriteImagesDao.getAll().map { it.map { it.uri } }

    override fun addToFavourites(uri: String) {
        CoroutineScope(Dispatchers.IO).launch { favouriteImagesDao.insertAll(Image(uri)) }
    }

    override fun removeFromFavourites(uri: String) {
        CoroutineScope(Dispatchers.IO).launch { favouriteImagesDao.delete(Image(uri)) }
    }
}
