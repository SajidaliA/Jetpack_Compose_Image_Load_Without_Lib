package com.example.sajidaliandroidassignment.di

import android.content.Context
import androidx.room.Room
import com.example.sajidaliandroidassignment.room.AppDatabase
import com.example.sajidaliandroidassignment.room.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
* Database module by hilt to provide AppDatabase and ImageDao
*/
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    //Provide AppDatabase object
    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext applicationContext: Context) : AppDatabase {
        return Room.databaseBuilder(applicationContext, AppDatabase::class.java, "image_database")
            .build()
    }

    //Provide ImageDAO object
    @Singleton
    @Provides
    fun provideImageDoo(appDatabase: AppDatabase) : ImageDao {
        return appDatabase.imageDao()
    }
}