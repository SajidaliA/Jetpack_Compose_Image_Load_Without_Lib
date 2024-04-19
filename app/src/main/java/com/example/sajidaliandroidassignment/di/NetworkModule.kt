package com.example.sajidaliandroidassignment.di

import com.example.sajidaliandroidassignment.network.api.ImageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Network module by hilt to provide Retrofit and ImageAPI service
 */

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    //Provide Retrofit object
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://acharyaprashant.org/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Provide ImageAPI object
    @Singleton
    @Provides
    fun provideImageAPI(retrofit: Retrofit) : ImageAPI {
        return retrofit.create(ImageAPI::class.java)
    }
}