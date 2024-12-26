package com.example.bracesbuddy.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.database.UserPreferences
import com.example.bracesbuddy.ui.theme.Colors
import com.example.bracesbuddy.ui.theme.Typography
import kotlinx.coroutines.launch
import java.util.Calendar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun SettingsScreen(db: AppDatabase) {
    val context = LocalContext.current
    val settingsDao = db.settingsDao()
    val coroutineScope = rememberCoroutineScope()

    var bracesDate by remember { mutableStateOf("") }
    var removalDate by remember { mutableStateOf("") }
    var showBracesDatePicker by remember { mutableStateOf(false) }
    var showRemovalDatePicker by remember { mutableStateOf(false) }
    var doctorName by remember { mutableStateOf("") }
    var clinicName by remember { mutableStateOf("") }
    var clinicPhone by remember { mutableStateOf("") }

//    var cleaningRemindersCount by remember { mutableStateOf(0) }
//    val cleaningReminderTimes = remember { mutableStateListOf<String>() }
//    val gson = Gson()
//
//    var visitRemindersEnabled by remember { mutableStateOf(false) }
//    var visitReminderDaysBefore by remember { mutableStateOf("") }
//    val daysOptions = listOf("1 день", "2 дні", "3 дні")
//
//    var photoRemindersEnabled by remember { mutableStateOf(false) }
//    var photoReminderFrequency by remember { mutableStateOf("") }
//    val frequencyOptions = listOf("Раз на день", "Раз на тиждень", "Раз на місяць")

    val userId = UserPreferences.getUserId(context)

    LaunchedEffect(userId) {
        val settings = settingsDao.getSettingsByUserId(userId)
        if (settings != null) {
            bracesDate = settings.bracesDate
            removalDate = settings.removalDate
            doctorName = settings.doctorName
            clinicName = settings.clinicName
            clinicPhone = settings.clinicPhone
//            cleaningRemindersCount = settings.cleaningRemindersCount
//            // Відновлення часу нагадувань про чистку
//            val reminderTimes = gson.fromJson(settings.cleaningReminderTimes, Array<String>::class.java)
//            cleaningReminderTimes.clear()
//            cleaningReminderTimes.addAll(reminderTimes)
//            visitRemindersEnabled = settings.visitRemindersEnabled
//            visitReminderDaysBefore = settings.visitReminderDaysBefore
//            photoRemindersEnabled = settings.photoRemindersEnabled
//            photoReminderFrequency = settings.photoReminderFrequency
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Background)
            .padding(start = 24.dp, end = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Брекети",
                style = Typography.titleLarge
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Дата встановлення брекетів:",
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Colors.InputFieldBackground)
                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp))
                    .clickable { showBracesDatePicker = true }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (bracesDate.isNotEmpty()) bracesDate else "Оберіть дату",
                    style = Typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Орієнтовна дата зняття брекетів:",
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Colors.InputFieldBackground)
                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp))
                    .clickable { showRemovalDatePicker = true }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (removalDate.isNotEmpty()) removalDate else "Оберіть дату",
                    style = Typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Colors.TitleColor,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Клініка",
                style = Typography.titleLarge
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Введіть ПІБ лікаря-ортодонта:",
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = doctorName,
                onValueChange = {
                    doctorName = it
                    coroutineScope.launch {
                        settingsDao.updateDoctorName(userId, doctorName)
                    }
                },
                label = { Text("ПІБ ортодонта", style = Typography.labelMedium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

            Text(
                text = "Введіть назву клініки:",
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = clinicName,
                onValueChange = {
                    clinicName = it
                    coroutineScope.launch {
                        settingsDao.updateClinicName(userId, clinicName)
                    }
                },
                label = { Text("Клініка", style = Typography.labelMedium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

            Text(
                text = "Введіть номер телефону клініки:",
                style = Typography.labelMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = clinicPhone,
                onValueChange = {
                    if (it.length <= 10) {
                        clinicPhone = it
                        coroutineScope.launch {
                            settingsDao.updateClinicPhone(userId, clinicPhone)
                        }
                    }
                },
                label = { Text("Номер телефону", style = Typography.labelMedium) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            HorizontalDivider(
//                color = Colors.TitleColor,
//                modifier = Modifier.padding(vertical = 16.dp)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Text(
//                text = "Сповіщення",
//                style = Typography.titleLarge
//            )
//
//            Spacer(modifier = Modifier.height(36.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.Start,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = "Чистка",
//                        style = Typography.bodyLarge
//                    )
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    Text(
//                        text = "Щоденне нагадування",
//                        style = Typography.labelMedium
//                    )
//                }
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    Button(onClick = {
//                        if (cleaningRemindersCount > 0) {
//                            cleaningRemindersCount--
//                            coroutineScope.launch {
//                                settingsDao.updateCleaningRemindersCount(userId, cleaningRemindersCount)
//                            }
//                        }
//                    }) {
//                        Text("-")
//                    }
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Text(
//                        text = "$cleaningRemindersCount",
//                        style = Typography.bodyMedium
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Button(onClick = {
//                        if (cleaningRemindersCount < 4) {
//                            cleaningRemindersCount++
//                            coroutineScope.launch {
//                                settingsDao.updateCleaningRemindersCount(userId, cleaningRemindersCount)
//                            }
//                        }
//                    }) {
//                        Text("+")
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .horizontalScroll(rememberScrollState())
//                    .padding(vertical = 8.dp)
//            ) {
//                for (i in 0 until cleaningRemindersCount) {
//                    val time = cleaningReminderTimes.getOrNull(i) ?: "08:00"
//                    val calendar = Calendar.getInstance()
//                    val initialHour = time.split(":")[0].toInt()
//                    val initialMinute = time.split(":")[1].toInt()
//
//                    val timePickerDialog = TimePickerDialog(
//                        context,
//                        { _, hourOfDay, minute ->
//                            val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
//
//                            if (i < cleaningReminderTimes.size) {
//                                cleaningReminderTimes[i] = formattedTime
//                            } else {
//                                cleaningReminderTimes.add(formattedTime)
//                            }
//
//                            val cleaningReminderTimesJson = gson.toJson(cleaningReminderTimes)
//
//                            coroutineScope.launch {
//                                settingsDao.updateCleaningReminderTimes(
//                                    userId,
//                                    cleaningReminderTimesJson
//                                )
//                            }
//                            scheduleCleaningReminders(context, cleaningReminderTimes)
//                        },
//                        initialHour,
//                        initialMinute,
//                        true
//                    )
//
//                    Box(
//                        modifier = Modifier
//                            .background(Colors.TransparentContainer, shape = RoundedCornerShape(20.dp))
//                            .clip(RoundedCornerShape(20.dp))
//                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp))
//                            .padding(16.dp)
//                            .clickable {
//                                timePickerDialog.show()
//                            }
//                    ) {
//                        Text(
//                            text = time,
//                            style = Typography.bodyMedium
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.Start,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = "Візити",
//                        style = Typography.bodyLarge
//                    )
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    Text(
//                        text = "Нагадати про візит за:",
//                        style = Typography.labelMedium
//                    )
//                }
//
//                Switch(
//                    checked = visitRemindersEnabled,
//                    onCheckedChange = { isChecked ->
//                        visitRemindersEnabled = isChecked
//                        coroutineScope.launch {
//                            settingsDao.updateVisitRemindersEnabled(
//                                userId,
//                                isChecked
//                            )
//                        }
//                    },
//                    colors = SwitchDefaults.colors(
//                        uncheckedThumbColor = Colors.TitleColor,
//                        checkedThumbColor = Colors.TitleColor,
//                        uncheckedTrackColor = Colors.TitleColor.copy(alpha = 0.5f),
//                        checkedTrackColor = Colors.TitleColor.copy(alpha = 0.8f)
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                daysOptions.forEach { day ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        RadioButton(
//                            selected = visitReminderDaysBefore == day,
//                            onClick = {
//                                visitReminderDaysBefore = day
//                                coroutineScope.launch {
//                                    settingsDao.updateVisitReminderDaysBefore(
//                                        userId,
//                                        day
//                                    )
//                                }
//                            },
//                            enabled = visitRemindersEnabled,
//                            colors = RadioButtonDefaults.colors(
//                                selectedColor = Colors.TitleColor,
//                                unselectedColor = Colors.TitleColor.copy(alpha = 0.6f)
//                            )
//                        )
//
//                        Text(
//                            text = day,
//                            style = Typography.bodyMedium,
//                            modifier = Modifier.padding(start = 8.dp)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(
//                    horizontalAlignment = Alignment.Start,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = "Фотографії",
//                        style = Typography.bodyLarge
//                    )
//                }
//
//                Switch(
//                    checked = photoRemindersEnabled,
//                    onCheckedChange = { isChecked ->
//                        photoRemindersEnabled = isChecked
//                        coroutineScope.launch {
//                            settingsDao.updatePhotoRemindersEnabled(
//                                userId,
//                                isChecked
//                            )
//                        }
//                    },
//                    colors = SwitchDefaults.colors(
//                        uncheckedThumbColor = Colors.TitleColor,
//                        checkedThumbColor = Colors.TitleColor,
//                        uncheckedTrackColor = Colors.TitleColor.copy(alpha = 0.5f),
//                        checkedTrackColor = Colors.TitleColor.copy(alpha = 0.8f)
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                frequencyOptions.forEach { frequency ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        RadioButton(
//                            selected = photoReminderFrequency == frequency,
//                            onClick = {
//                                photoReminderFrequency = frequency
//                                coroutineScope.launch {
//                                    settingsDao.updatePhotoReminderFrequency(
//                                        userId,
//                                        frequency
//                                    )
//                                }
//                            },
//                            enabled = photoRemindersEnabled,
//                            colors = RadioButtonDefaults.colors(
//                                selectedColor = Colors.TitleColor,
//                                unselectedColor = Colors.TitleColor.copy(alpha = 0.6f)
//                            )
//                        )
//
//                        Text(
//                            text = frequency,
//                            style = Typography.bodyMedium,
//                            modifier = Modifier.padding(start = 8.dp)
//                        )
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(18.dp))

            if (showBracesDatePicker) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        bracesDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)

                        coroutineScope.launch {
                            settingsDao.updateBracesDate(
                                userId,
                                bracesDate
                            )
                        }
                        showBracesDatePicker = false
                    },
                    year,
                    month,
                    day
                ).apply {
                    setOnDismissListener {
                        showBracesDatePicker = false
                    }
                }.show()
            }

            if (showRemovalDatePicker) {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val newRemovalDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)

                        if (newRemovalDate != removalDate) {
                            removalDate = newRemovalDate

                            coroutineScope.launch {
                                settingsDao.updateRemovalDate(
                                    userId,
                                    removalDate
                                )
                            }
                        }

                        showRemovalDatePicker = false
                    },
                    year,
                    month,
                    day
                ).apply {
                    setOnDismissListener {
                        showRemovalDatePicker = false
                    }
                }.show()
            }
        }
    }
}
