package com.fiky.lofo_app.composables

import android.view.MotionEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

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
                marker.title = "Lokasi Terakhir Barang"
                overlays.add(marker)

                //Self Location
                val locationProvider = GpsMyLocationProvider(context)
                val myLocationOverlay = MyLocationNewOverlay(locationProvider, this)

                // Enables Self Location
                myLocationOverlay.enableMyLocation()
                // Opsional: Jika ingin otomatis zoom ke arah kita, aktifkan ini:
                 myLocationOverlay.enableFollowLocation()
                myLocationOverlay.setDrawAccuracyEnabled(true)

                overlays.add(myLocationOverlay)

                // Logic handle scroll agar tidak bentrok dengan parent
                setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            v.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        MotionEvent.ACTION_UP -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    false
                }
            }
        },
        update = { mapView ->
            val point = GeoPoint(lat, lon)
            mapView.controller.setCenter(point)

            mapView.onResume()
        }
    )
}