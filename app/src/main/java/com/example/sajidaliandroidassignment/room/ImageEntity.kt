package com.example.sajidaliandroidassignment.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Image entity for Room DB
 */

@Entity
data class ImageEntity(
    @PrimaryKey
    val id : String,
    val image: Bitmap?
)
