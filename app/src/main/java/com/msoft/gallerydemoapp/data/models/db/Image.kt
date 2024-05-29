package com.msoft.gallerydemoapp.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(@PrimaryKey val uri: String)
