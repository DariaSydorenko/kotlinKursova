package com.example.bracesbuddy.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.database.PhotosDao
import com.example.bracesbuddy.data.database.UserPreferences
import com.example.bracesbuddy.utils.savePhoto
import com.example.bracesbuddy.ui.theme.Colors
import com.example.bracesbuddy.ui.theme.Typography

@Composable
fun AlbumScreen(db: AppDatabase) {
    val photosDao: PhotosDao = db.photosDao()
    val context = LocalContext.current
    val userId = UserPreferences.getUserId(context)
    val coroutineScope = rememberCoroutineScope()

    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val pickPhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            savePhoto(it, photosDao, userId, coroutineScope)
        }
    }

    val permissionGranted = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    val photos by photosDao.getAllPhotos(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Background)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (permissionGranted) {
                    pickPhotoLauncher.launch("image/*")
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        (context as? ComponentActivity)?.let { activity ->
                            activity.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
        ) {
            Text(text = "Додати фотографію", style = Typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Мій фотоальбом",
            style = Typography.titleLarge
        )

        if (photos.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Поки немає жодної фотографії",
                    style = Typography.bodyLarge
                )
            }
        } else {
            photos.forEach { photo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photo.photoUri),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                selectedPhotoUri = Uri.parse(photo.photoUri)
                                isDialogOpen = true
                            },
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = photo.photoDate,
                        style = Typography.bodyMedium
                    )
                }
            }
        }

        if (isDialogOpen) {
            Dialog(onDismissRequest = { isDialogOpen = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedPhotoUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { isDialogOpen = false },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}