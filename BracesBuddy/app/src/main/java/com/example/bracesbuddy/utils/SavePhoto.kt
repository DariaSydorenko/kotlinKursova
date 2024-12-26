package com.example.bracesbuddy.utils

import android.net.Uri
import com.example.bracesbuddy.data.database.PhotosDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

fun savePhoto(uri: Uri, photosDao: PhotosDao, userId: Int, coroutineScope: CoroutineScope) {
    val photoDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    coroutineScope.launch {
        photosDao.insertPhoto(userId, uri.toString(), photoDate)
    }
}