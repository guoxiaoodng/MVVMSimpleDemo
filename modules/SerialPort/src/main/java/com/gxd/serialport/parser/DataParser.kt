package com.gxd.serialport.parser

import android.annotation.SuppressLint
import android.util.Log
import com.gxd.serialport.bean.BloodPressure

/**
 * 数据解析工具
 * 参考：瑞思迈血压计RBP-7000通讯协议V1.0
 * @author gxd
 * @date 2025/10/15
 */
@SuppressLint("DefaultLocale")
object DataParser {
    private const val TAG = "DataParser"

    /**
     * 解析测量时间（文档表4：6字节，Dat5=年-2000，Dat4=月，Dat3=日，Dat2=时，Dat1=分，Dat0=秒）
     */
    fun parseTime(timeData: ByteArray): String {
        if (timeData.size != 6) {
            Log.e(TAG, "时间数据长度错误（需6字节），当前长度：${timeData.size}")
            return "时间解析失败"
        }
        val year = 2000 + (timeData[0].toInt() and 0xFF) // Dat5
        val month = timeData[1].toInt() and 0xFF // Dat4
        val day = timeData[2].toInt() and 0xFF // Dat3
        val hour = timeData[3].toInt() and 0xFF // Dat2
        val minute = timeData[4].toInt() and 0xFF // Dat1
        val second = timeData[5].toInt() and 0xFF // Dat0
        // 格式化（补0，确保格式统一）
        return String.format(
                "%04d-%02d-%02d %02d:%02d:%02d",
                year, month, day, hour, minute, second
        )
    }

    /**
     * 解析血压值（文档表5：6字节，SYS=Dat5*256+Dat4，DIA=Dat3*256+Dat2，PUL=Dat1*256+Dat0）
     */
    fun parseBloodPressure(bloodData: ByteArray): BloodPressure {
        if (bloodData.size != 6) {
            Log.e(TAG, "血压数据长度错误（需6字节），当前长度：${bloodData.size}")
            return BloodPressure(-1, -1, -1)
        }
        // 收缩压（SYS：高8位<<8 + 低8位）
        val sys = (bloodData[0].toInt() and 0xFF) shl 8 or (bloodData[1].toInt() and 0xFF)
        // 舒张压（DIA）
        val dia = (bloodData[2].toInt() and 0xFF) shl 8 or (bloodData[3].toInt() and 0xFF)
        // 脉搏（PUL）
        val pul = (bloodData[4].toInt() and 0xFF) shl 8 or (bloodData[5].toInt() and 0xFF)
        return BloodPressure(sys, dia, pul)
    }

    /**
     * 解析设备编码（文档表11：7字节，Dat6+Dat5=机型，Dat4=年份，Dat3=月份，Dat2=批次，Dat1+Dat0=序号）
     */
    fun parseDeviceCode(codeData: ByteArray): String {
        if (codeData.size != 7) {
            Log.e(TAG, "设备编码数据长度错误（需7字节），当前长度：${codeData.size}")
            return "设备编码解析失败"
        }
        // 机型编码（Dat6+Dat5，十六进制转字符串）
        val model = String.format(
                "%02X%02X",
                codeData[0].toInt() and 0xFF,
                codeData[1].toInt() and 0xFF
        )
        // 年份（Dat4=年份后两位，如0x12→18→2018）
        val year = 2000 + (codeData[2].toInt() and 0xFF)
        // 月份（Dat3）
        val month = codeData[3].toInt() and 0xFF
        // 批次（Dat2）
        val batch = codeData[4].toInt() and 0xFF
        // 序号（Dat1*256+Dat0）
        val serial = (codeData[5].toInt() and 0xFF) shl 8 or (codeData[6].toInt() and 0xFF)
        return String.format("%s-%04d%02d-%02d-%d", model, year, month, batch, serial)
    }

    /**
     * 构建时间设置数据（文档4.3，按表4格式：6字节）
     */
    fun buildTimeData(
            year: Int,
            month: Int,
            day: Int,
            hour: Int,
            minute: Int,
            second: Int
    ): ByteArray {
        return byteArrayOf(
                (year - 2000).toByte(), // Dat5：年-2000
                month.toByte(), // Dat4：月
                day.toByte(), // Dat3：日
                hour.toByte(), // Dat2：时
                minute.toByte(), // Dat1：分
                second.toByte() // Dat0：秒
        )
    }

    /**
     * 字节数组转十六进制字符串（调试用）
     */
    fun bytesToHexString(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X ", b))
        }
        return sb.toString().trim()
    }
}