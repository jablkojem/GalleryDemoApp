package com.msoft.gallerydemoapp.di.module

import android.app.Application
import androidx.room.Room
import com.msoft.gallerydemoapp.data.datasource.dao.FavouriteImagesDao
import com.msoft.gallerydemoapp.data.datasource.FavouriteImagesDatabase
import com.msoft.gallerydemoapp.data.datasource.LocalImagesDataSource
import com.msoft.gallerydemoapp.data.repository.FavouriteImagesRepository
import com.msoft.gallerydemoapp.data.repository.FavouriteImagesRepositoryImpl
import com.msoft.gallerydemoapp.data.repository.ImagesRepository
import com.msoft.gallerydemoapp.data.repository.ImagesRepositoryImpl
import com.msoft.gallerydemoapp.domain.use_case.FormatDateUseCase
import com.msoft.gallerydemoapp.utils.FAVOURITE_IMAGES_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppManagersModule {

    @Singleton
    @Provides
    fun provideLocalImagesDataSource(application: Application): LocalImagesDataSource {
        return LocalImagesDataSource(application)
    }

    @Singleton
    @Provides
    fun provideFavouriteImagesDatabase(application: Application): FavouriteImagesDatabase {
        return Room.databaseBuilder(application, FavouriteImagesDatabase::class.java, FAVOURITE_IMAGES_DB_NAME).build()
    }

    @Provides
    fun provideFavouriteImagesDao(database: FavouriteImagesDatabase): FavouriteImagesDao {
        return database.favouriteImagesDao()
    }

    @Singleton
    @Provides
    fun provideImagesRepository(
        application: Application,
        localImagesDataSource: LocalImagesDataSource,
        formatDateUseCase: FormatDateUseCase
    ): ImagesRepository {
        return ImagesRepositoryImpl(application, localImagesDataSource, formatDateUseCase)
    }

    @Singleton
    @Provides
    fun provideFavouriteImagesRepository(
        favouriteImagesDao: FavouriteImagesDao
    ): FavouriteImagesRepository {
        return FavouriteImagesRepositoryImpl(favouriteImagesDao)
    }
}