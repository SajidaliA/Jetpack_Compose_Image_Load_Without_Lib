package com.example.sajidaliandroidassignment.network.api

import com.example.sajidaliandroidassignment.network.response.ImagesResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Image API for retrofit API call end points
 */

interface ImageAPI {

    //API end point for image API with limit of 100 results
    @GET("content/misc/media-coverages")
    suspend fun getImages(@Query("limit") limit: Int) : Response<List<ImagesResponseItem>>
}