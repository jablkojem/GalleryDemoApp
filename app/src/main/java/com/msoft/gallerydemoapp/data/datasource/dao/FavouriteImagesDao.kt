package com.msoft.gallerydemoapp.data.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msoft.gallerydemoapp.data.models.db.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteImagesDao {
    @Query("SELECT * FROM image")
    fun getAll(): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg images: Image)

    @Delete
    fun delete(image: Image)
}
