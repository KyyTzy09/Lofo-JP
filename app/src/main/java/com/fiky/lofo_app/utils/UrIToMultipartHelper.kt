package com.fiky.lofo_app.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object UrIToMultipartHelper {
    private fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
       val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }

    fun uploadImage(context: Context, uri: Uri, name: String): MultipartBody.Part {
        val file = getFileFromUri(context, uri)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(name, file.name, requestFile)
        return body
    }
}