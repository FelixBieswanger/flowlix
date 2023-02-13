package com.example.flowlix.presentation.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.flowlix.data.FlowUIState

@Composable
fun NoActivityScreen(flowUIState: State<FlowUIState>){
    Text(
        textAlign = TextAlign.Center,
        text = "No Action requiered at the moment!"
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        textAlign = TextAlign.Center,
        fontSize = 10.sp,
        fontStyle = FontStyle.Italic,
        text = "Next Alarm: ${flowUIState.value.nextAlarm}")
}