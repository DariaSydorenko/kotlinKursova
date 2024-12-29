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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.database.PhotosDao
import com.example.bracesbuddy.data.database.UserPreferences
import com.example.bracesbuddy.ui.icons.Icons
import com.example.bracesbuddy.utils.savePhoto
import com.example.bracesbuddy.ui.theme.Colors
import com.example.bracesbuddy.ui.theme.Typography
import kotlinx.coroutines.launch

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

    // Function to delete photo
    fun deletePhoto(photoId: Int) {
        coroutineScope.launch {
            photosDao.deletePhoto(photoId)
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Background)
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                        pickPhotoLauncher.launch("image/*")
                    } else {
                        (context as? ComponentActivity)?.let { activity ->
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                1
                            )
                        }
                    }
                } else {
                    pickPhotoLauncher.launch("image/*")
                }
            },
            modifier = Modifier
                .height(80.dp)
                .align(Alignment.CenterHorizontally)
                .border(1.dp, Colors.TitleColor, RoundedCornerShape(50)),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
        ) {
            Icon(
                Icons.Add(),
                contentDescription = "Додати фото",
                tint = Colors.TitleColor,
                modifier = Modifier.size(60.dp)
            )
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
                        .padding(vertical = 12.dp),
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = photo.photoDate,
                            style = Typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { deletePhoto(photo.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Colors.NavBarSelectColor),
                            modifier = Modifier.border(1.dp, Colors.TitleColor, RoundedCornerShape(50)),
                        ) {
                            Icon(
                                Icons.Delete(),
                                contentDescription = "Видалити фото",
                                tint = Colors.TitleColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
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