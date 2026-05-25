package com.fiky.lofo_app.utils

fun TextLimiter(text: String): String {
    // Trim spasi di awal/akhir, lalu pecah berdasarkan spasi kosong beruntun
    val words = text.trim().split(Regex("\\s+"))

    return if (words.size > 200) {
        // Ambil 200 kata pertama saja, lalu gabungkan kembali dengan spasi
        words.take(200).joinToString(" ")
    } else {
        text
    }
}