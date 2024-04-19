package com.example.sajidaliandroidassignment.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sajidaliandroidassignment.util.Converter

/**
 * Database abstract class Room DB
 */

@Database(entities = [ImageEntity::class], version = 1)
//Type converters added to tell Room that how to convert Bitmap to bytearray and vice-versa
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}