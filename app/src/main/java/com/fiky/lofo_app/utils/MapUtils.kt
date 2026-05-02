package com.fiky.lofo_app.utils
//
//import kotlinx.coroutines.CoroutineScope
//import org.osmdroid.bonuspack.routing.OSRMRoadManager
//import org.osmdroid.bonuspack.routing.RoadManager
//import org.osmdroid.views.overlay.Polyline
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.osmdroid.util.GeoPoint
//import org.osmdroid.views.MapView
//
//object MapUtils {
//    fun drawRoute(mapView: MapView, start: GeoPoint, end: GeoPoint) {
//        val roadManager: RoadManager = OSRMRoadManager(mapView.context, "userAgent")
//
//        // Routing harus dijalankan di background thread (Worker Thread)
//        CoroutineScope(Dispatchers.IO).launch {
//            val waypoints = arrayListOf(start, end)
//            val road = roadManager.getRoad(waypoints)
//            val roadOverlay = RoadManager.buildRoadOverlay(road)
//
//            withContext(Dispatchers.Main) {
//                mapView.overlays.add(roadOverlay)
//                mapView.invalidate() // Refresh peta
//            }
//        }
//    }
//}