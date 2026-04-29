package com.fiky.lofo_app.composables

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMap(
    lat: Double,
    lon: Double,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            MapView(context).apply {
                setMultiTouchControls(true)
                controller.setZoom(16.0)

                val startPoint = GeoPoint(lat, lon)
                controller.setCenter(startPoint)

                val marker = Marker(this)
                marker.position = startPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Lokasi Terakhir"
                overlays.add(marker)

                setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            // Minta parent untuk tidak melakukan intercept touch
                            v.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_UP -> {
                            // Kembalikan kontrol ke parent
                            v.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    // Tetap jalankan fungsi touch bawaan MapView
                    false
                }
            }
        },
        update = { mapView ->
            val point = GeoPoint(lat, lon)
            mapView.controller.setCenter(point)
        }
    )
}