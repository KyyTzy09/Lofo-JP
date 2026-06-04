package com.fiky.lofo_app.utils

object QRHelper {
    fun generateLocalQRCode(content: String): android.graphics.Bitmap {
        val size = 512
        val hints = hashMapOf<com.google.zxing.EncodeHintType, Any>().apply {
            put(com.google.zxing.EncodeHintType.MARGIN, 1) // Ketebalan border putih
        }
        val bits = com.google.zxing.qrcode.QRCodeWriter().encode(
            content,
            com.google.zxing.BarcodeFormat.QR_CODE,
            size,
            size,
            hints
        )
        return android.graphics.Bitmap.createBitmap(size, size, android.graphics.Bitmap.Config.RGB_565).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    setPixel(
                        x, y,
                        if (bits[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    )
                }
            }
        }
    }
}