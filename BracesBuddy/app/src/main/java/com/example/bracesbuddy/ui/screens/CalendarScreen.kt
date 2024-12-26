package com.example.bracesbuddy.ui.screens

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.database.UserPreferences
import com.example.bracesbuddy.ui.icons.Icons
import com.example.bracesbuddy.ui.theme.Colors
import com.example.bracesbuddy.ui.theme.Typography
import com.example.bracesbuddy.data.model.Visits
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(db: AppDatabase) {
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf("") }
    var visitName by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val visitsDao = db.visitsDao()
    val userId = UserPreferences.getUserId(context)

    val visitsInMonth = remember(currentMonth) {
        mutableStateOf(emptyList<Visits>())
    }

    LaunchedEffect(currentMonth) {
        visitsInMonth.value = visitsDao.getVisitsInMonth(userId, currentMonth.toString())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Background, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
                .border(1.dp, Colors.CalendarColor, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Colors.CalendarColor)
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(
                            Icons.Arrow(),
                            contentDescription = "Попередній місяць",
                            tint = Colors.Background,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("uk", "UA"))
                            .replaceFirstChar { it.uppercase() } + " " + currentMonth.year,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                        textAlign = TextAlign.Center,
                        color = Colors.Background
                    )
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(
                            Icons.Arrow(),
                            contentDescription = "Наступний місяць",
                            tint = Colors.Background,
                            modifier = Modifier
                                .graphicsLayer(rotationZ = 180f)
                                .size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд")
                    daysOfWeek.forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = Colors.CalendarColor
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val daysInMonth = currentMonth.lengthOfMonth()
                    val firstDayOfWeek = (currentMonth.atDay(1).dayOfWeek.value % 7 + 6) % 7
                    var currentDay = 1

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (week in 0..5) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (day in 0..6) {
                                    if ((week == 0 && day < firstDayOfWeek) || currentDay > daysInMonth) {
                                        Box(modifier = Modifier.weight(1f)) {}
                                    } else {
                                        val date = currentMonth.atDay(currentDay)
                                        val hasVisit = visitsInMonth.value.any { visit ->
                                            visit.visitDate == date.toString()
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .background(
                                                    color = when {
                                                        date.isEqual(today) -> Colors.SubtitleColor
                                                        hasVisit -> Colors.ButtonBackground // фон для дня з візитом
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .border(1.dp, Colors.CalendarColor)
                                                .clickable {
                                                    selectedDate = date
                                                    isDialogOpen = true
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = currentDay.toString(),
                                                style = Typography.bodyMedium,
                                                color = if (hasVisit) Colors.Background else Colors.TitleColor
                                            )
                                        }
                                        currentDay++
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (isDialogOpen && selectedDate != null) {
            Dialog(onDismissRequest = { isDialogOpen = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Colors.Background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val visitExist = remember { mutableStateOf(false) }
                        val existingVisit = remember { mutableStateOf<Visits?>(null) }

                        LaunchedEffect(selectedDate) {
                            selectedDate?.let { date ->
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val formattedDate = date.format(formatter)

                                val visit = visitsDao.getVisitByDate(userId, formattedDate)
                                if (visit != null) {
                                    visitExist.value = true
                                    existingVisit.value = visit
                                } else {
                                    visitExist.value = false
                                }
                            }
                        }

                        if (visitExist.value) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Запланований візит",
                                    style = Typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "\"${existingVisit.value?.visitName}\"\n" +
                                            "${existingVisit.value?.visitDate} о ${existingVisit.value?.visitTime}",
                                    style = Typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            val isEditing = remember { mutableStateOf(false) }

                            if (isEditing.value) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    var visitName by remember { mutableStateOf("") }
                                    var selectedTime by remember { mutableStateOf("") }

                                    LaunchedEffect(selectedDate) {
                                        selectedDate?.let { date ->
                                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                            val formattedDate = date.format(formatter)

                                            val currentVisit = visitsDao.getVisitByDate(userId, formattedDate)
                                            currentVisit?.let {
                                                visitName = it.visitName
                                                selectedTime = it.visitTime
                                            }
                                        }
                                    }

                                    TextField(
                                        value = visitName,
                                        onValueChange = { visitName = it },
                                        label = {
                                            Text(
                                                "Назва візиту",
                                                style = Typography.bodyMedium
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                Color.Transparent,
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .border(
                                                1.dp,
                                                Colors.TitleColor,
                                                RoundedCornerShape(20.dp)
                                            ),
                                        textStyle = Typography.bodyMedium,
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Colors.InputFieldBackground,
                                            unfocusedContainerColor = Colors.InputFieldBackground,
                                            disabledContainerColor = Colors.InputFieldBackground,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    val context = LocalContext.current

                                    val timePickerDialog = TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                                        },
                                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                        Calendar.getInstance().get(Calendar.MINUTE),
                                        true
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(
                                                Color.Transparent,
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .border(
                                                1.dp,
                                                Colors.TitleColor,
                                                RoundedCornerShape(20.dp)
                                            )
                                            .padding(16.dp)
                                            .clickable {
                                                timePickerDialog.show()
                                            }
                                    ) {
                                        Text(
                                            text = if (selectedTime.isNotEmpty()) selectedTime else "Час візиту",
                                            style = Typography.bodyMedium
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = {
                                            if (visitName.isNotBlank() && selectedTime.isNotBlank()) {
                                                coroutineScope.launch {
                                                    selectedDate?.let { date ->
                                                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                        val formattedDate = date.format(formatter)

                                                        visitsDao.updateVisit(
                                                            visitId = existingVisit.value?.id ?: 0,
                                                            name = visitName,
                                                            date = formattedDate,
                                                            time = selectedTime
                                                        )
                                                    }
                                                }
                                                isEditing.value = false
                                                isDialogOpen = false
                                                Toast.makeText(
                                                    context,
                                                    "Візит оновлено",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .border(
                                                1.dp,
                                                Colors.TitleColor,
                                                RoundedCornerShape(30.dp)
                                            ),
                                        colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
                                    ) {
                                        Text("Зберегти зміни", style = Typography.bodyLarge)
                                    }
                                }
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Button(
                                        onClick = { isEditing.value = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .border(
                                                1.dp,
                                                Colors.TitleColor,
                                                RoundedCornerShape(320.dp)
                                            ),
                                        colors = ButtonDefaults.buttonColors(containerColor = Colors.NavBarColor)
                                    ) {
                                        Text("Редагувати", style = Typography.bodyLarge)
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = {
                                            coroutineScope.launch {
                                                existingVisit.value?.id?.let { visitId ->
                                                    visitsDao.deleteVisit(visitId)
                                                    visitsInMonth.value = visitsDao.getVisitsInMonth(userId, currentMonth.toString())
                                                }
                                            }
                                            isDialogOpen = false
                                            Toast.makeText(context, "Візит видалено", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .border(1.dp, Colors.TitleColor, RoundedCornerShape(30.dp)),
                                        colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
                                    ) {
                                        Text("Видалити", style = Typography.bodyLarge)
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "На цей день запланованих візитів немає",
                                style = Typography.titleLarge,
                                textAlign = TextAlign.Center
                            )

                            HorizontalDivider(
                                color = Colors.TitleColor,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            TextField(
                                value = visitName,
                                onValueChange = { visitName = it },
                                label = { Text("Назва візиту", style = Typography.bodyMedium) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Color.Transparent,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                                textStyle = Typography.bodyMedium,
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Colors.InputFieldBackground,
                                    unfocusedContainerColor = Colors.InputFieldBackground,
                                    disabledContainerColor = Colors.InputFieldBackground,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                            val context = LocalContext.current
                            var selectedTime by remember { mutableStateOf("") }

                            val timePickerDialog = TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                                },
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                Calendar.getInstance().get(Calendar.MINUTE),
                                true
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Color.Transparent,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp))
                                    .padding(16.dp)
                                    .clickable {
                                        timePickerDialog.show()
                                    }
                            ) {
                                Text(
                                    text = if (selectedTime.isNotEmpty()) selectedTime else "Час візиту",
                                    style = Typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    if (visitName.isNotBlank() && selectedTime.isNotBlank()) {
                                        coroutineScope.launch {
                                            selectedDate?.let { date ->
                                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                val formattedDate = date.format(formatter)

                                                visitsDao.insertVisit(
                                                    userId = userId,
                                                    name = visitName,
                                                    date = formattedDate,
                                                    time = selectedTime
                                                )
                                                visitsInMonth.value = visitsDao.getVisitsInMonth(userId, currentMonth.toString())
                                            }
                                        }
                                        isDialogOpen = false
                                        Toast.makeText(context, "Візит додано", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(30.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Colors.ButtonBackground)
                            ) {
                                Text("Додати візит", style = Typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}