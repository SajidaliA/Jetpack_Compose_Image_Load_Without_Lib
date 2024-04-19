package com.example.sajidaliandroidassignment.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * DAO interface to write queries for Room DB
 */

@Dao
interface ImageDao {

    //Upsert will add new Image entity to Room DB and replace if its already exist
    @Upsert
    suspend fun upsertImage(image: ImageEntity)

    //Get specific Image entity by id
    @Query("SELECT * FROM imageentity WHERE id LIKE :id")
    suspend fun getLocalImageById(id: String): ImageEntity

    //Get all the images from Room DB
    @Query("SELECT * FROM imageentity")
    suspend fun getLocalImages(): List<ImageEntity>
}