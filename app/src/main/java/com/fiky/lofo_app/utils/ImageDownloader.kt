package com.fiky.lofo_app.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class ImageDownloader(private val context: Context) {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadFile(url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setMimeType("image/jpeg") // Sesuaikan dengan format gambar
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        downloadManager.enqueue(request)
    }
}