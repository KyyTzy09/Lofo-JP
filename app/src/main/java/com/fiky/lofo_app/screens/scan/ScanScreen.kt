package com.fiky.lofo_app.screens.scan

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    viewModel: ScanViewModel = viewModel(),
    onNavigateToDetail: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val state =viewModel.state

    // PENGUNCI NAVIGASI: Mencegah navigasi dipicu berkali-kali secara simultan
    var isScanned by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updatePermission(isGranted)
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val context = LocalContext.current

    val onQrScannedFromCamera = { qrCode: String ->
        viewModel.onQrCodeDetected(
            context = context,
            code = qrCode,
            onCodeScanned = { code, lat, lon ->
                onNavigateToDetail(code)
            },
            onError = { errorMessage ->
                println("Log Error: $errorMessage")
            }
        )
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isCameraPermissionGranted) {
                CameraPreview(
                    onQrCodeScanned = { code ->
                        // Hanya eksekusi jika belum sukses mendeteksi sebelumnya
                        if (!isScanned) {
                            isScanned = true
                            onQrScannedFromCamera(code)
                        }
                    },
                    flashlightOn = state.isFlashlightOn
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Camera Permission Required", color = Color.White)
                }
            }

            // Layer Overlay Hitam Transparan
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    ScannerCorners()
                    ScanningLine()

                    Icon(
                        Icons.Default.QrCode2,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .blur(2.dp),
                        tint = Color.White.copy(alpha = 0.4f)
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = if (isScanned) "Processing..." else "Ready to Scan",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Posisikan QR barangmu di kotak",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    onQrCodeScanned: (String) -> Unit,
    flashlightOn: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Memastikan Executor dan PreviewView tidak dibuat ulang saat recomposition
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember { PreviewView(context) }

    // Menyimpan referensi cameraControl untuk flash toggle
    val cameraControlState = remember { mutableStateOf<androidx.camera.core.CameraControl?>(null) }

    // Efek Senter: Mengontrol flash secara dinamis tanpa re-bind CameraProvider
    LaunchedEffect(flashlightOn) {
        cameraControlState.value?.enableTorch(flashlightOn)
    }

    AndroidView(
        factory = { previewView },
        update = { _ ->
            // Inisialisasi kamera hanya berjalan sekali saat pertama kali dipasang
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { imageProxy ->
                            processImageProxy(imageProxy, onQrCodeScanned)
                        }
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                    // Simpan kontrol kamera untuk efek senter di atas
                    cameraControlState.value = camera.cameraControl
                    camera.cameraControl.enableTorch(flashlightOn)
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        },
        modifier = Modifier.fillMaxSize()
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQrCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Menggunakan break/return setelah barcode pertama didapat untuk efisiensi instan
                val barcode = barcodes.firstOrNull()
                barcode?.rawValue?.let { code ->
                    onQrCodeScanned(code)
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}
@Composable
fun ScanActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = Color.White
        )
    }
}

@Composable
fun ScannerCorners() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 4.dp.toPx()
        val cornerSize = 40.dp.toPx()
        val radius = 24.dp.toPx()

        // Top Left
        val topLeftPath = Path().apply {
            moveTo(0f, cornerSize)
            lineTo(0f, radius)
            quadraticBezierTo(0f, 0f, radius, 0f)
            lineTo(cornerSize, 0f)
        }
        drawPath(topLeftPath, Color.White, style = Stroke(strokeWidth))

        // Top Right
        val topRightPath = Path().apply {
            moveTo(size.width - cornerSize, 0f)
            lineTo(size.width - radius, 0f)
            quadraticBezierTo(size.width, 0f, size.width, radius)
            lineTo(size.width, cornerSize)
        }
        drawPath(topRightPath, Color.White, style = Stroke(strokeWidth))

        // Bottom Left
        val bottomLeftPath = Path().apply {
            moveTo(0f, size.height - cornerSize)
            lineTo(0f, size.height - radius)
            quadraticBezierTo(0f, size.height, radius, size.height)
            lineTo(cornerSize, size.height)
        }
        drawPath(bottomLeftPath, Color.White, style = Stroke(strokeWidth))

        // Bottom Right
        val bottomRightPath = Path().apply {
            moveTo(size.width - cornerSize, size.height)
            lineTo(size.width - radius, size.height)
            quadraticBezierTo(size.width, size.height, size.width, size.height - radius)
            lineTo(size.width, size.height - cornerSize)
        }
        drawPath(bottomRightPath, Color.White, style = Stroke(strokeWidth))
    }
}

@Composable
fun ScanningLine() {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter)
                .offset(y = (offsetY * 260).dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, Color.White, Color.Transparent)
                    )
                )
        )
    }
}
