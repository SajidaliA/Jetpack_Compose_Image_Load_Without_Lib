package com.example.sajidaliandroidassignment.view

import android.annotation.SuppressLint
import android.database.CursorWindow
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sajidaliandroidassignment.R
import com.example.sajidaliandroidassignment.network.response.ImagesResponseItem
import com.example.sajidaliandroidassignment.room.ImageEntity
import com.example.sajidaliandroidassignment.ui.theme.SajidAliAndroidAssignmentTheme
import com.example.sajidaliandroidassignment.util.LRUCache
import com.example.sajidaliandroidassignment.util.constructUrl
import com.example.sajidaliandroidassignment.util.getPlaceholderBitmap
import com.example.sajidaliandroidassignment.util.isInternetAvailable
import com.example.sajidaliandroidassignment.util.loadImageFromUrl
import com.example.sajidaliandroidassignment.viewmodel.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imageViewModel: ImageViewModel by viewModels()

    // mLRUCache for cache bitmaps to memory
    private val mLRUCache = LRUCache()
    private var isInternet = false


    @SuppressLint("DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SajidAliAndroidAssignmentTheme {

                // Prevent larger row size in room DB
                try {
                    val field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
                    field.isAccessible = true
                    field.set(null, 100 * 1024 * 1024)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Main Image grid composable
                    ImagesGrid()
                }
            }
        }
    }

    @Composable
    fun ImagesGrid() {
        isInternet = isInternetAvailable(LocalContext.current)

        //Check internet and get data based on that
        if (isInternet) {
            //Fetch server images
            imageViewModel.getServerImages()
        } else {
            //Fetch local image (Disk cache)
            imageViewModel.getLocalImages()
        }
        //Collect server images
        val images: State<List<ImagesResponseItem>> =
            imageViewModel.images.collectAsState()

        //Collect local images
        val localImages: State<List<ImageEntity>?> =
            imageViewModel.localImages.collectAsState()

        //Show no data view if no any data found
        val showNoData = remember {
            mutableStateOf(false)
        }

        //Composable to show Lazy vertical grid
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            //Set 3 column
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(5.dp)
        ) {
            if (isInternet) {
                //Hide no data view
                showNoData.value = false

                //Show grid by server images
                items(images.value) { item ->
                    var bitmap by remember {
                        mutableStateOf<Bitmap?>(null)
                    }
                    LaunchedEffect(item.thumbnail.id) {
                        //Get bitmap from memory cache (LRU cache)
                        bitmap = mLRUCache.retrieveBitmapFromCache(item.thumbnail.id)
                        if (bitmap == null) {
                           //Get bitmap from Disk cache (Room DB)
                            bitmap = imageViewModel.getLocalImageById(item.thumbnail.id)
                                .await()?.image
                            bitmap?.let {
                                //Save local bitmap to memory cache (LRU cache)
                                mLRUCache.saveBitmapToCache(item.thumbnail.id, it)
                            }
                            if (bitmap == null) {
                                //Create bitmap from server image URL
                                bitmap =
                                    loadImageFromUrl(constructUrl(item.thumbnail)).await()
                                bitmap?.let{
                                    //Save bitmap to memory cache (LRU cache)
                                    mLRUCache.saveBitmapToCache(item.thumbnail.id, it)
                                    //Save bitmap to disk cache (Room DB)
                                    imageViewModel.saveImageToDB(
                                        ImageEntity(
                                            item.thumbnail.id,
                                            it
                                        )
                                    )
                                }
                            }
                        }
                    }
                    if (bitmap == null) {
                        //Get placeholder bitmap in case of null bitmap
                        bitmap = getPlaceholderBitmap(LocalContext.current)
                    }
                    //Finally load bitmap into image
                    LoadBitmap(bitmap)

                }
            //If internet not available then check for local images disk cache (Room DB)
            } else if (!localImages.value.isNullOrEmpty()) {
                //Hide no data view
                showNoData.value = false

                //Show grid by local images
                items(localImages.value!!) { imageEntity ->
                    //Get state fro bitmap
                    var bitmap by remember {
                        mutableStateOf<Bitmap?>(null)
                    }

                    //Launch effect to perform coroutines
                    LaunchedEffect(imageEntity.id) {
                        //Get bitmap from memory cache (LRU cache)
                        bitmap = mLRUCache.retrieveBitmapFromCache(imageEntity.id)
                        if (bitmap == null) {
                            //Get bitmap from Disk cache (Room DB)
                            bitmap = imageViewModel.getLocalImageById(imageEntity.id)
                                .await()?.image
                            bitmap?.let {
                                //Save bitmap to memory cache (LRU cache)
                                mLRUCache.saveBitmapToCache(imageEntity.id, it)
                            }
                        }
                    }
                    if (bitmap == null) {
                        //Get placeholder bitmap in case of null bitmap
                        bitmap = getPlaceholderBitmap(LocalContext.current)
                    }
                    //Finally load bitmap into image
                    LoadBitmap(bitmap)
                }
            }else{
                //Show no data view
               showNoData.value = true
            }

        }
        if (showNoData.value) {
            //Show no data view based on no data state
            ShowNoData()
        }
    }

    //Composable to show Grid Image Item
    @Composable
    fun LoadBitmap(bitmap: Bitmap?) {
        bitmap?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = "Thumbnail Image",
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )
        }
    }

    //Composable to show no data
    @Composable
    fun ShowNoData() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_data),
                contentDescription = "No data",
                modifier = Modifier
                    .size(150.dp)
                    .padding(10.dp)
            )
            Text(
                text = stringResource(id = R.string.no_data_found),
                color = Color.LightGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    this@MainActivity.recreate()
                }, modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.retry),
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                )
            }
        }

    }
}






