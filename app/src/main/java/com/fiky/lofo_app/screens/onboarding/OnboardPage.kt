package com.fiky.lofo_app.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingPage(page: Int) {
    when (page) {
        0 -> StepOne()
        1 -> StepTwo()
        2 -> StepThreeFour()
        3 -> StepFive()
    }
}

@Composable
fun StepOne() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xFF14002F).copy(alpha = 0.6f))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    "STEP 01",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Add Item",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Register your premium belongings. Provide descriptions and photos to create a unique digital identity for each item.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StepTwo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    "STEP 02",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Secure QR Code",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "A unique encrypted QR code is generated. Download and attach it to your item via our premium tags or custom stickers.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StepThreeFour() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "STEP 03 & 04",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Smart Status Management",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Lost an item? One-tap to update status to Lost. Once recovered, toggle it back to Returned to resume protection.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.surfaceContainer)) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(8.dp).background(MaterialTheme.colorScheme.primary))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.surfaceContainer)) {
                Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(MaterialTheme.colorScheme.secondaryContainer))
            }
        }
    }
}

@Composable
fun StepFive() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkGray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
             Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "STEP 05",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Instant Connection",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "When someone finds your item, scanning the QR code brings them directly to a secure detail page with your recovery instructions.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Encrypted Data Protection", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Location-Ping Reporting", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}
