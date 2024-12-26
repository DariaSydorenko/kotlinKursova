package com.example.bracesbuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bracesbuddy.data.database.AppDatabase
import com.example.bracesbuddy.data.database.UserPreferences
import com.example.bracesbuddy.data.model.Visits
import com.example.bracesbuddy.ui.theme.Colors
import com.example.bracesbuddy.ui.theme.Typography
import com.example.bracesbuddy.utils.formatDate
import com.example.bracesbuddy.utils.parseDate
import java.util.*

// Utility Functions
fun getPluralForm(number: Int, singular: String, few: String, many: String): String {
    return when {
        number % 100 in 11..19 -> many
        number % 10 == 1 -> singular
        number % 10 in 2..4 -> few
        else -> many
    }
}

fun calculateDateDifference(startDate: Date?, endDate: Date?): Int {
    if (startDate == null || endDate == null) return 0
    val startCalendar = Calendar.getInstance().apply { time = startDate }
    val endCalendar = Calendar.getInstance().apply { time = endDate }
    return ((endCalendar.timeInMillis - startCalendar.timeInMillis) / (1000 * 60 * 60 * 24)).toInt() + 1
}

fun formatDaysAsYearsMonthsDays(days: Int): String {
    val years = days / 365
    val months = (days % 365) / 30
    val remainingDays = (days % 365) % 30

    return buildString {
        if (years > 0) append("$years ${getPluralForm(years, "рік", "роки", "років")}, ")
        if (months > 0) append("$months ${getPluralForm(months, "місяць", "місяці", "місяців")}, ")
        append("$remainingDays ${getPluralForm(remainingDays, "день", "дні", "днів")}")
    }
}

@Composable
fun HomeScreen(db: AppDatabase) {
    val context = LocalContext.current
    val settingsDao = db.settingsDao()
    val visitsDao = db.visitsDao()

    val userId = UserPreferences.getUserId(context)

    val bracesDate = remember { mutableStateOf<String?>(null) }
    val removalDate = remember { mutableStateOf<String?>(null) }
    val visits = remember { mutableStateListOf<Visits>() }

    LaunchedEffect(userId) {
        bracesDate.value = settingsDao.getBracesDateByUserId(userId)
        removalDate.value = settingsDao.getRemovalDateByUserId(userId)
        visits.clear()
        visits.addAll(visitsDao.getVisitsByUserId(userId))
    }

    val bracesDateParsed = bracesDate.value?.let { parseDate(it) }
    val removalDateParsed = removalDate.value?.let { parseDate(it) }
    val currentDate = Calendar.getInstance().time

    val isRemovalDateAfterBracesDate = removalDateParsed?.after(bracesDateParsed) ?: true
    val isBracesDateInFuture = bracesDateParsed?.after(currentDate) ?: false

    if (isBracesDateInFuture) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Background)
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Дата встановлення брекетів не може бути в майбутньому!\nПеревірте дату і повертайтесь)",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    } else if (!isRemovalDateAfterBracesDate) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Background)
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Неправильно вказано дані про встановлення/зняття брекетів!\nПеревірте дати і повертайтесь)",
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    } else {
        val daysInBraces = calculateDateDifference(bracesDateParsed, currentDate)
        val daysUntilRemoval = calculateDateDifference(currentDate, removalDateParsed)

        val progressPercentage = if (bracesDateParsed != null && removalDateParsed != null) {
            val totalDays = calculateDateDifference(bracesDateParsed, removalDateParsed)
            (daysInBraces.toFloat() / totalDays.toFloat()) * 100f
        } else {
            0f
        }

        val showProgress = bracesDateParsed != null && removalDateParsed != null

        val nextVisit = visits.filter {
            val visitDate = parseDate(it.visitDate)
            visitDate != null && visitDate.after(currentDate)
        }.minByOrNull { parseDate(it.visitDate)?.time ?: Long.MAX_VALUE }

        val nextVisitDays = nextVisit?.visitDate?.let { visitDate ->
            val visitDateParsed = parseDate(visitDate)
            calculateDateDifference(currentDate, visitDateParsed)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Background)
                .padding(start = 24.dp, end = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!showProgress) {
                Text(
                    text = "Заповнюй дату встановлення та орієнтовану дату зняття брекетів і повертайся сюди, щоб слідкувати за прогресом!",
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { progressPercentage / 100f },
                            modifier = Modifier.fillMaxSize(),
                            color = Colors.TitleColor,
                            strokeWidth = 30.dp,
                            trackColor = Colors.SubtitleColor,
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (removalDateParsed == null || currentDate < removalDateParsed) {
                                    "Ви в брекетах вже:"
                                } else {
                                    "Ви були в брекетах:"
                                },
                                style = Typography.labelMedium,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = formatDaysAsYearsMonthsDays(daysInBraces),
                                style = Typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (removalDateParsed != null) {
                        if (currentDate < removalDateParsed) {
                            Text(
                                text = "До зняття брекетів:",
                                style = Typography.labelMedium
                            )

                            Text(
                                text = formatDaysAsYearsMonthsDays(daysUntilRemoval),
                                style = Typography.titleLarge
                            )
                        } else {
                            Text(
                                text = "Ви вже не в брекетах",
                                style = Typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        color = Colors.TitleColor,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (nextVisit != null && nextVisitDays != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Наступний візит:",
                                style = Typography.bodyLarge,
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 20.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, Colors.TitleColor, RoundedCornerShape(20.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Colors.SubtitleColor)
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Через $nextVisitDays ${getPluralForm(nextVisitDays, "день", "дні", "днів")}",
                                            style = Typography.bodyLarge.copy(
                                                color = Colors.Background
                                            )
                                        )
                                    }

                                    Text(
                                        text = formatDate(nextVisit.visitDate),
                                        style = Typography.bodyMedium.copy(color = Colors.TitleColor),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )

                                    Text(
                                        text = nextVisit.visitTime,
                                        style = Typography.bodyLarge.copy(color = Colors.TitleColor),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Поки немає запланованих візитів",
                            style = Typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        color = Colors.TitleColor,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = "Історія візитів",
                            style = Typography.titleLarge,
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        if (visits.isEmpty()) {
                            Text(
                                text = "Ваша історія візитів порожня",
                                style = Typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            visits.sortedByDescending { parseDate(it.visitDate)?.time }
                                .forEach { visit ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(15.dp)
                                                .background(Colors.TitleColor, CircleShape)
                                        )

                                        Spacer(modifier = Modifier.width(20.dp))

                                        Column {
                                            Text(
                                                text = visit.visitName,
                                                style = Typography.bodyLarge,
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = "${visit.visitDate} ${visit.visitTime}",
                                                style = Typography.labelMedium,
                                            )
                                        }
                                    }
                                }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
