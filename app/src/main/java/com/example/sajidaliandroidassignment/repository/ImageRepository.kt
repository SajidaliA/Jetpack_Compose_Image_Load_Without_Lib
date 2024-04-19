package com.example.sajidaliandroidassignment.repository

import com.example.sajidaliandroidassignment.network.api.ImageAPI
import com.example.sajidaliandroidassignment.network.response.ImagesResponseItem
import com.example.sajidaliandroidassignment.room.ImageDao
import com.example.sajidaliandroidassignment.room.ImageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Image repository class to get Images from server and local Disk cache (Room DB)
 */

class ImageRepository @Inject constructor(private val imageAPI: ImageAPI, private val imageDao: ImageDao) {

    //Private flow variable that only modify by this repo.
    private val _images = MutableStateFlow<List<ImagesResponseItem>>(emptyList())

    //Public flow variable to read data from _images
    val images: StateFlow<List<ImagesResponseItem>>
        get() = _images

    //Get images from server
    suspend fun getImages(limit: Int){
        val response = imageAPI.getImages(limit)
        //Check if response is successful and response body is not empty
        if (response.isSuccessful && !response.body().isNullOrEmpty()){
            //emit data to _images flow
            response.body()?.let { _images.emit(it) }
        }
    }
    //Get single image from Disk cache (Room DB)
    suspend fun getLocalImageById(id:String): ImageEntity = imageDao.getLocalImageById(id)

    //Get all images from Disk cache (Room DB)
    suspend fun getLocalImages(): List<ImageEntity> = imageDao.getLocalImages()

    //Save image to Disk cache (Room DB)
    suspend fun saveImageToDB(imageEntity: ImageEntity){
        imageDao.upsertImage(imageEntity)
    }
}