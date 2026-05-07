package com.fiky.lofo_app.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TextUtils {
    fun formatIndonesianPhone(phone: String?): String {
        if (phone.isNullOrBlank()) return "-"
        // Menghapus karakter non-digit kecuali tanda + di awal
        val onlyDigits = phone.replace(Regex("[^\\d+]"), "")
        val cleanNumber = onlyDigits.replaceFirst("^62".toRegex(), "0")
        return cleanNumber.replace("(\\d{4})(?=\\d)".toRegex(), "$1-")
    }

    fun formatToIndonesianDate(
        dateString: String?,
        inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", // Sesuaikan dengan format API-mu
        outputPattern: String = "dd MMMM yyyy"
    ): String {
        if (dateString.isNullOrBlank()) return "-"

        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault()).apply {
                // Biasanya format 'Z' di ujung berarti UTC
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val outputFormat = SimpleDateFormat(outputPattern, Locale("id", "ID"))

            val date = inputFormat.parse(dateString)
            if (date != null) outputFormat.format(date) else "-"
        } catch (e: Exception) {
            // Jika parsing gagal, kembalikan string aslinya atau default
            dateString
        }
    }
}