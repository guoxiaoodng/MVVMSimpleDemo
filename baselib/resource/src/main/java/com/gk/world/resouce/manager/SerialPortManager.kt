package com.gk.world.resouce.manager

import android.serialport.SerialPort
import com.blankj.utilcode.util.ToastUtils
import okio.IOException
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.io.OutputStream

object SerialPortManager {
    private const val TAG = "SerialPortManager";
    private var serialPort: SerialPort? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var isRunning: Boolean = false

    /**
     * 打开串口
     * @param devicePath 串口设备路径（如 "/dev/ttyS1"）
     * @param baudRate 波特率（如 9600）
     */
    fun open(devicePath: String, baudRate: Int): Boolean {
        return try {
            val device = File(devicePath)
            //检查设备是否存在且可读可写
            if (!device.exists() || !device.canRead() || !device.canWrite()) {
                ToastUtils.showLong("串口设备不可访问")
                return false
            }
            serialPort = SerialPort(device, baudRate)
            inputStream = serialPort?.inputStream
            outputStream = serialPort?.outputStream
            isRunning = true
            startReadThread()
            true
        } catch (e: Exception) {
            Timber.tag(TAG).e("打开串口失败: ${e.message}")
            ToastUtils.showLong("串口打开失败")
            false
        }
    }

    /**
     * 关闭串口
     */
    fun close() {
        isRunning = false
        try {
            inputStream?.close()
            outputStream?.close()
            serialPort?.close()
        } catch (e: IOException) {
            Timber.tag(TAG).e("关闭串口失败: ${e.message}")
            ToastUtils.showLong("串口关闭失败")
        }
        inputStream?.close()
        outputStream?.close()
        serialPort?.close()
    }

    /**
     * 发送数据
     */
    fun sendData(data: ByteArray): Boolean {
        return try {
            outputStream?.write(data)
            outputStream?.flush()
            true
        } catch (e: IOException) {
            Timber.tag(TAG).e("发送数据失败: ${e.message}")
            false
        }
    }

    private fun startReadThread() {
        Thread {
            val buffer = ByteArray(1024)
            while (isRunning) {
                try {
                    val size = inputStream?.read(buffer) ?: -1
                    if (size > 0) {
                        val receivedData = buffer.copyOf(size)
                        // 回调处理接收到的数据（可通过接口回调到UI层）
                        onDataReceived?.invoke(receivedData)
                        //处理接收到的数据
                        Timber.tag(TAG).d("接收到数据: ${receivedData.joinToString(",")}")
                    }
                } catch (e: IOException) {
                    Timber.tag(TAG).e("读取数据失败: ${e.message}")
                    isRunning = false
                }
            }
        }
    }

    var onDataReceived: ((data: ByteArray) -> Unit)? = null
}