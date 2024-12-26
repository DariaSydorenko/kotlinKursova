package com.example.bracesbuddy.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.bracesbuddy.ui.theme.Colors
import androidx.compose.material3.*
import com.example.bracesbuddy.R

@Composable
fun Logo(modifier: Modifier = Modifier) {
    val robotoBold = FontFamily(Font(R.font.roboto_bold))

    Text(
        text = buildAnnotatedString {
            append("B")
            withStyle(SpanStyle(color = Colors.TitleColor.copy(alpha = 0.7f))) { append("races") }
            append("B")
            withStyle(SpanStyle(color = Colors.TitleColor.copy(alpha = 0.7f))) { append("uddy") }
        },
        style = MaterialTheme.typography.titleLarge.copy(
            fontFamily = robotoBold,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        ),
        modifier = modifier,
        color = Colors.TitleColor
    )
}
