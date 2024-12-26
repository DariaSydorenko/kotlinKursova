package com.example.bracesbuddy.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.model.User
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
import com.example.bracesbuddy.ui.theme.Colors
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.bracesbuddy.ui.components.Logo
import com.example.bracesbuddy.ui.theme.Typography
import com.example.bracesbuddy.utils.isValidEmail
import com.example.bracesbuddy.data.model.Settings
import kotlinx.coroutines.delay

@Composable
fun RegistrationScreen(navController: NavController, db: AppDatabase) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val userDao = db.userDao()
    val settingsDao = db.settingsDao()
    val coroutineScope = rememberCoroutineScope()

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
                    .clip(RoundedCornerShape(30.dp))
                    .background(Colors.TransparentContainer)
                    .padding(32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "РЕЄСТРАЦІЯ",
                        style = Typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Ім'я", style = Typography.labelMedium) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Colors.InputFieldBackground)
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = androidx.compose.ui.text.input.ImeAction.Next
                        ),
                        textStyle = Typography.bodyMedium,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Colors.TitleColor
                        )
                    )

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Електронна пошта", style = Typography.labelMedium) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Colors.InputFieldBackground)
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = Typography.bodyMedium,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Colors.TitleColor
                        )
                    )

                    TextField(
                        value = password,
                        onValueChange = {
                            if (it.length in 4..20) password = it
                            coroutineScope.launch {
                                passwordVisible = true
                                delay(1000L)
                                passwordVisible = false
                            }
                        },
                        label = { Text("Пароль (від 4 до 20 символів)", style = Typography.labelMedium) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Colors.InputFieldBackground)
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = Typography.bodyMedium,
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Colors.TitleColor
                        )
                    )

                    TextField(
                        value = confirmPassword,
                        onValueChange = {
                            if (it.length <= 20) confirmPassword = it
                        },
                        label = { Text("Повторно пароль", style = Typography.labelMedium) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Colors.InputFieldBackground)
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = Typography.bodyMedium,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = Colors.TitleColor
                        )
                    )

                    Button(
                        onClick = {
                            if (password == confirmPassword && password.length in 4..20) {
                                if (isValidEmail(email)) {
                                    coroutineScope.launch {
                                        val user = userDao.getUser(email, password)
                                        if (user == null) {
                                            val newUser = User(name = name, email = email, password = password)
                                            userDao.insertUser(newUser)

                                            val defaultSettings = Settings(
                                                userId = userDao.getUserId(email = email, password = password),
                                                bracesDate = "", // Example default values
                                                removalDate = "",
                                                doctorName = "",
                                                clinicName = "",
                                                clinicPhone = "",
//                                                cleaningRemindersCount = 0,
//                                                cleaningReminderTimes = "[]",
//                                                visitRemindersEnabled = false,
//                                                visitReminderDaysBefore = "",
//                                                photoRemindersEnabled = false,
//                                                photoReminderFrequency = ""
                                            )
                                            settingsDao.insertSettings(defaultSettings)

                                            Toast.makeText(
                                                navController.context,
                                                "Акаунт створено!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("login")
                                        } else {
                                            Toast.makeText(
                                                navController.context,
                                                "Користувач з такою поштою вже існує",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        navController.context,
                                        "Неправильний формат електронної пошти",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    navController.context,
                                    "Паролі не співпадають або не відповідають вимогам",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(50)),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
                    ) {
                        Text(
                            text = "Створити акаунт",
                            style = Typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Вже є акаунт?\nПовернутися до входу",
                        style = Typography.bodyMedium.copy(
                            color = Colors.HighlightColor,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .clickable {
                                navController.navigate("login")
                            }
                    )
                }
            }
        }
    }
}
