package com.example.sajidaliandroidassignment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sajidaliandroidassignment.network.response.ImagesResponseItem
import com.example.sajidaliandroidassignment.repository.ImageRepository
import com.example.sajidaliandroidassignment.room.ImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model will handle the API call initiate by UI and pass it to the repository
 * and get the result from repository and pass to the UI
 *
 * This view model is handled by Hilt (DI)
 */
@HiltViewModel
class ImageViewModel @Inject constructor(private val repository: ImageRepository) : ViewModel() {

    //Get server images from repository
    val images: StateFlow<List<ImagesResponseItem>>
        get() = repository.images

    //Get local images from repository
    private val _localImages = MutableStateFlow<List<ImageEntity>?>(emptyList())
    val localImages: StateFlow<List<ImageEntity>?>
        get() = _localImages

    //Exception handler by coroutine in case of API failure
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }

    //Fetch server image
   fun getServerImages(){
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) { repository.getImages(100) }
    }

    //Fetch all local images from disk cache (Room DB)
    fun getLocalImages() {
        var imageEntities : List<ImageEntity>? = null
        viewModelScope.launch {
            try {
                imageEntities = repository.getLocalImages()
            }catch (e: Exception){
                Log.e("ROOM", "Failed to get local Images: ${e.message}")
            }
            if (imageEntities?.isNotEmpty() == true){
                _localImages.emit(imageEntities)
            }
        }
    }

    //Fetch only one image from local disk cache (Room DB)
    fun getLocalImageById(id:String): Deferred<ImageEntity?> {
        return viewModelScope.async(Dispatchers.IO){
            try {
                val imageEntity = repository.getLocalImageById(id)
                imageEntity
            }catch (e: Exception){
                Log.e("ROOM", "Failed to get local Image: ${e.message}")
                null
            }
        }
    }

    //Save image to local disk cache (Room DB)
    fun saveImageToDB(imageEntity: ImageEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveImageToDB(imageEntity)
        }
    }
}