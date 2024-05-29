package com.msoft.gallerydemoapp.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.msoft.gallerydemoapp.data.datasource.dao.FavouriteImagesDao
import com.msoft.gallerydemoapp.data.models.db.Image

@Database(entities = [Image::class], version = 1)
abstract class FavouriteImagesDatabase : RoomDatabase() {
    abstract fun favouriteImagesDao(): FavouriteImagesDao
}
