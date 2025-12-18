package com.gk.world.comprehensive.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRCodeUtils {

    fun generateQRCodeBitmap(
        content: String,
        width: Int = 800,
        height: Int = 800
    ): Bitmap {
        val hints = hashMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = 1

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    fun generateQRCodeWithLogo(
        content: String,
        logo: Bitmap? = null,
        size: Int = 800
    ): Bitmap {
        val qrCode = generateQRCodeBitmap(content, size, size)

        if (logo == null) return qrCode

        val logoSize = size / 5
        val scaledLogo = Bitmap.createScaledBitmap(logo, logoSize, logoSize, false)

        val offsetX = (size - logoSize) / 2
        val offsetY = (size - logoSize) / 2

        val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        // 绘制二维码
        canvas.drawBitmap(qrCode, 0f, 0f, null)

        // 绘制白色背景
        val paint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
        }
        canvas.drawRect(
            offsetX.toFloat() - 5,
            offsetY.toFloat() - 5,
            offsetX + logoSize + 5f,
            offsetY + logoSize + 5f,
            paint
        )

        // 绘制Logo
        canvas.drawBitmap(scaledLogo, offsetX.toFloat(), offsetY.toFloat(), null)

        return result
    }
}