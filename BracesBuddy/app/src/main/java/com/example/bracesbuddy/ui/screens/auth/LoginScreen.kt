package com.example.bracesbuddy.ui.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.ui.theme.Colors
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.bracesbuddy.data.database.UserPreferences
import com.example.bracesbuddy.ui.components.Logo
import com.example.bracesbuddy.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(navController: NavController, db: AppDatabase, context: Context) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val userDao = db.userDao()
    val coroutineScope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Background)
    ) {
        Logo(modifier = Modifier.padding(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Colors.TransparentContainer, shape = RoundedCornerShape(30.dp))
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Вхід",
                        style = Typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Електронна пошта", style = Typography.labelMedium) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = Typography.bodyMedium,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Colors.InputFieldBackground,
                            unfocusedContainerColor = Colors.InputFieldBackground,
                            disabledContainerColor = Colors.InputFieldBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Colors.TitleColor
                        )
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    TextField(
                        value = password,
                        onValueChange = {
                            if (it.length <= 20) password = it
                            coroutineScope.launch {
                                passwordVisible = true
                                delay(1000L)
                                passwordVisible = false
                            }
                        },
                        label = { Text("Пароль", style = Typography.labelMedium)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = Typography.bodyMedium,
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Colors.InputFieldBackground,
                            unfocusedContainerColor = Colors.InputFieldBackground,
                            disabledContainerColor = Colors.InputFieldBackground,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Colors.TitleColor
                        )
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val user = userDao.getUser(email, password)
                                if (user != null) {
                                    UserPreferences.saveUserId(context, user.id)
                                    Toast.makeText(
                                        navController.context,
                                        "Вхід успішний!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate("home")
                                } else {
                                    Toast.makeText(
                                        navController.context,
                                        "Некоректні дані",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
                    ) {
                        Text(
                            text = "Увійти",
                            style = Typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Немає акаунту?\nПерехід до реєстрації",
                        style = Typography.bodyMedium.copy(
                            color = Colors.HighlightColor,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                navController.navigate("registration")
                            }
                    )
                }
            }
        }
    }
}
