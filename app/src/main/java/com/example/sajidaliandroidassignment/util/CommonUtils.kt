package com.example.sajidaliandroidassignment.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.example.sajidaliandroidassignment.R
import com.example.sajidaliandroidassignment.network.response.Thumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.net.HttpURLConnection
import java.net.URL

/**
 * CommonUtil is use for general functions that can be used anywhere in the project
 */

//Get the bitmap from server image URL
fun loadImageFromUrl(url: String): Deferred<Bitmap?> {
   return CoroutineScope(Dispatchers.IO).async {
       try {
           val connection = URL(url).openConnection() as HttpURLConnection
           val inputStream = connection.inputStream
           inputStream.mark(inputStream.available())
           val bitmap = BitmapFactory.decodeStream(inputStream)
           inputStream.close()
           bitmap
       } catch (e: Exception) {
           Log.e("IMAGE_LOADING", "Failed to load image : $e")
          null
       }
   }
}

//Get the placeholder image in case of null bitmap
fun getPlaceholderBitmap(context: Context): Bitmap? {
    var placeholder: Bitmap? = null

    val drawable: Drawable? =
        ResourcesCompat.getDrawable(context.resources, R.drawable.placeholder, null)
    drawable?.let {
        if (it is BitmapDrawable) {
            if (it.bitmap != null) {
                placeholder = it.bitmap
            }
        } else if (it.intrinsicWidth <= 0 || it.intrinsicHeight <= 0) {
            placeholder = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            placeholder =
                Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        val mutableBitmap = placeholder?.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = mutableBitmap?.let { it1 -> Canvas(it1) }
        if (canvas != null) {
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
        }
    }

    return placeholder
}

//Construct the final image URL
fun constructUrl(thumbnail: Thumbnail): String {
    return thumbnail.domain + "/" + thumbnail.basePath + "/0/" + thumbnail.key
}

// Check is internet available or not
fun isInternetAvailable(context: Context): Boolean {
    val result: Boolean
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}