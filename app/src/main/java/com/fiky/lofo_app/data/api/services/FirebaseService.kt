package com.fiky.lofo_app.data.api.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.fiky.lofo_app.MainActivity

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // PELACAK 1: Cek apakah data dari Google beneran masuk ke dalam HP kamu
        Log.d("FCM_LOFO", "============= NOTIF VALID MASUK HP =============")
        Log.d("FCM_LOFO", "From: ${message.from}")
        Log.d("FCM_LOFO", "Notification Title: ${message.notification?.title}")
        Log.d("FCM_LOFO", "Notification Body: ${message.notification?.body}")
        Log.d("FCM_LOFO", "Data Payload (jika ada): ${message.data}")

        // PELACAK 2: Tetap panggil notification builder
        showNotification(
            message.notification?.title ?: "LoFo Reminder",
            message.notification?.body ?: "Ada pengumuman baru nih!"
        )
    }

    private fun showNotification(title: String, body: String) {
        try {
            val channelId = "lofo_reminder_channel_v2" // Ganti nama channel biar ke-reset di OS
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Reminder Pengumuman LoFo",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
                Log.d("FCM_LOFO", "PELACAK: Notification Channel Berhasil Dibuat/Dicek")
            }

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // PELACAK 3: Kita ganti .setSmallIcon ke icon dialog bawaan android OS murni
            // Ini untuk memastikan 100% gak ada silent crash gara-gara file icon_lofo korup/salah format
            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Pakai icon sistem dulu jur!
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(999, notification)
            Log.d("FCM_LOFO", "PELACAK: fungsi notificationManager.notify() SUKSES DIJALANKAN!")

        } catch (e: Exception) {
            Log.e("FCM_LOFO", "PELACAK ERROR: Gagal me-render notifikasi fisik!", e)
        }
    }
}