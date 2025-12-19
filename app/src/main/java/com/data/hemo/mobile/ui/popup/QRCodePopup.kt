package com.data.hemo.mobile.ui.popup

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.ToastUtils
import com.data.hemo.mobile.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView

class QRCodePopup(context: Context): CenterPopupView(context) {

    private lateinit var qrResult: AppCompatTextView
    private var qrData: String = ""

    companion object {
        fun show(context: Context,
                 qrData: String) {
            XPopup.Builder(context)
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(QRCodePopup(context).apply {
                    setQrData(qrData)
                })
                .show()
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.popup_qr_code
    }

    override fun onCreate() {
        super.onCreate()
        val qrImage = findViewById<AppCompatImageView>(R.id.iv_qrcode)
        qrResult = findViewById(R.id.tv_qr_result)

        generateQRCode(qrData, qrImage)
    }

    private fun generateQRCode(data: String, imageView: AppCompatImageView) {
        try {
            val writer = MultiFormatWriter()
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 400, 400)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("二维码生成失败，请重试")
        }
    }

    fun setQrData(data: String) {
        qrData = data
    }
}