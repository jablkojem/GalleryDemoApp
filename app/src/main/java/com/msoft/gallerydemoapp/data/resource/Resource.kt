package com.msoft.gallerydemoapp.data.resource

sealed class Resource<out T> {
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    data class Data<out T>(val data: T) : Resource<T>()
}
