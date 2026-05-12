package com.fiky.lofo_app.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingCard(content: @Composable ColumnScope.() -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp) // <-- INI KUNCINYA (fix height)
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween // biar rapi
    ) {
        content()
    }
}