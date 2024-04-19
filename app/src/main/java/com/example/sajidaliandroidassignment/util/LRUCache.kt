package com.example.sajidaliandroidassignment.util

import android.graphics.Bitmap
import android.util.LruCache

/**
 * LRU cache is use to cache the data to memory cache and it will handle OOM (Out Of Memory)
 */

class LRUCache {
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    private val cacheSize = maxMemory / 8
    private val lru = LruCache<Any, Any>(cacheSize)

    //Save bitmap to LRU cache (Memory cache)
    fun saveBitmapToCache(key: String, bitmap: Bitmap) {
        //Only save bitmap to cache if its not already exist
        if (lru.get(key) == null){
            lru.put(key, bitmap)
        }
    }

    //Get bitmap from LRU cache (Memory cache)
    fun retrieveBitmapFromCache(key: String): Bitmap? {
        if (lru.get(key) != null) {
            return lru.get(key) as Bitmap?
        }
        return null
    }
}