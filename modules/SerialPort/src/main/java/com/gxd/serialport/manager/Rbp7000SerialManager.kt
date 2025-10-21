package com.gxd.serialport.manager

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import android.serialport.SerialPort
import com.gxd.serialport.constants.Rbp7000ProtocolConst.CHECKSUM_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DATA_BITS
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DATA_LEN_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DEFAULT_BAUD_RATE
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DEVICE_VERSION_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.FLOW_CONTROL
import com.gxd.serialport.constants.Rbp7000ProtocolConst.HEADER_DEVICE_RESP
import com.gxd.serialport.constants.Rbp7000ProtocolConst.HEADER_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.PARITY
import com.gxd.serialport.constants.Rbp7000ProtocolConst.STOP_BITS
import com.gxd.serialport.parser.DataParser
import com.gxd.serialport.tools.ProtocolFrameTool
import com.gxd.serialport.tools.SerialDataListener
import kotlinx.coroutines.cancel

/**
 * RBP-7000串口管理（基于android-serialport-api）
 * 参考：瑞思迈血压计RBP-7000通讯协议V1.0
 * @author gxd
 * @date 2025/10/15
 */
class Rbp7000SerialManager(
        private val devicePath: String, // 串口设备路径（需根据硬件确定，文档不定义）
        private val baudRate: Int = DEFAULT_BAUD_RATE,
        private val listener: SerialDataListener
) {
    private val TAG = "Rbp7000SerialManager"
    private var serialPort: SerialPort? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private var isConnected = false

    // 协程作用域（处理异步读写，替代线程池）
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val receiveBuffer = ByteArray(1024) // 接收缓存（解决粘包）
    private var bufferLen = 0

    /**
     * 打开串口（按文档参数配置）
     */
    fun openSerialPort() {
        if (isConnected) {
            Log.w(TAG, "串口已连接，无需重复打开")
            listener.onConnected(true)
            return
        }
        try {
            // 1. 初始化串口（文档1-6：8N1无流控）
            val serialFile = File(devicePath)
            serialPort = SerialPort(
                    serialFile,
                    baudRate,
                    DATA_BITS,
                    STOP_BITS,
                    PARITY,
                    FLOW_CONTROL
            )
            // 2. 获取流
            outputStream = serialPort?.outputStream
            inputStream = serialPort?.inputStream
            isConnected = true
            Log.d(TAG, "串口打开成功：路径=$devicePath，波特率=$baudRate")
            listener.onConnected(true)
            // 3. 启动接收协程
            startReceiveCoroutine()
        } catch (e: SecurityException) {
            Log.e(TAG, "串口权限不足：${e.message}")
            listener.onError("串口权限不足：${e.message}")
            listener.onConnected(false)
        } catch (e: IOException) {
            Log.e(TAG, "串口打开失败：${e.message}")
            listener.onError("串口打开失败：${e.message}")
            listener.onConnected(false)
        }
    }

    /**
     * 启动接收协程（持续读取设备数据）
     */
    private fun startReceiveCoroutine() {
        coroutineScope.launch {
            while (isConnected && inputStream != null) {
                try {
                    // 读取数据到缓存
                    val readLen = inputStream?.read(receiveBuffer, bufferLen, receiveBuffer.size - bufferLen) ?: 0
                    if (readLen > 0) {
                        bufferLen += readLen
                        // 解析缓存中的完整帧
                        parseReceiveBuffer()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "串口接收异常：${e.message}")
                    closeSerialPort()
                    listener.onError("串口接收异常：${e.message}")
                    listener.onConnected(false)
                    break
                }
            }
        }
    }

    /**
     * 解析接收缓存（提取完整帧，解决粘包拆包）
     */
    private fun parseReceiveBuffer() {
        while (bufferLen >= HEADER_LENGTH) {
            // 1. 查找前导码（设备发送前导码固定为0xAA,0x80）
            val headerIndex = findHeaderIndex()
            if (headerIndex == -1) {
                // 无有效前导码，清空缓存
                bufferLen = 0
                break
            }
            // 2. 移动缓存（保留前导码开始的数据）
            if (headerIndex > 0) {
                System.arraycopy(receiveBuffer, headerIndex, receiveBuffer, 0, bufferLen - headerIndex)
                bufferLen -= headerIndex
            }
            // 3. 检查帧最小长度（前导码+设备版本+数据长度=4字节）
            if (bufferLen < HEADER_LENGTH + DEVICE_VERSION_LENGTH + DATA_LEN_LENGTH) {
                break // 数据未完整，等待后续字节
            }
            // 4. 提取数据长度，计算完整帧长度
            val dataLen = receiveBuffer[HEADER_LENGTH + DEVICE_VERSION_LENGTH].toInt() and 0xFF
            val totalFrameLen = HEADER_LENGTH + DEVICE_VERSION_LENGTH + DATA_LEN_LENGTH + dataLen + CHECKSUM_LENGTH
            if (bufferLen < totalFrameLen) {
                break // 帧未完整，等待后续字节
            }
            // 5. 提取完整帧
            val fullFrame = receiveBuffer.copyOfRange(0, totalFrameLen)
            // 6. 更新缓存（移除已解析的帧）
            bufferLen -= totalFrameLen
            if (bufferLen > 0) {
                System.arraycopy(receiveBuffer, totalFrameLen, receiveBuffer, 0, bufferLen)
            }
            // 7. 解析完整帧
            ProtocolFrameTool.parseReceiveFrame(fullFrame, listener)
        }
    }

    /**
     * 查找前导码在缓存中的起始索引
     */
    private fun findHeaderIndex(): Int {
        for (i in 0..bufferLen - HEADER_LENGTH) {
            if (receiveBuffer[i] == HEADER_DEVICE_RESP[0] && receiveBuffer[i + 1] == HEADER_DEVICE_RESP[1]) {
                return i
            }
        }
        return -1
    }

    /**
     * 发送指令（PC端→设备）
     */
    fun sendCommand(type: Byte, subType: Byte, dataContent: ByteArray = byteArrayOf()) {
        if (!isConnected || outputStream == null) {
            Log.w(TAG, "串口未连接，无法发送指令")
            listener.onError("串口未连接，无法发送指令")
            return
        }
        coroutineScope.launch {
            try {
                // 1. 组装指令帧
                val sendFrame = ProtocolFrameTool.buildSendFrame(type, subType, dataContent)
                // 2. 发送数据
                outputStream?.write(sendFrame)
                outputStream?.flush()
                Log.d(TAG, "发送指令成功：${DataParser.bytesToHexString(sendFrame)}")
            } catch (e: IOException) {
                Log.e(TAG, "指令发送失败：${e.message}")
                listener.onError("指令发送失败：${e.message}")
            }
        }
    }

    /**
     * 关闭串口
     */
    fun closeSerialPort() {
        if (!isConnected) return
        isConnected = false
        // 取消协程
        coroutineScope.cancel()
        // 关闭流和串口
        try {
            inputStream?.close()
            outputStream?.close()
            serialPort?.close()
        } catch (e: IOException) {
            Log.e(TAG, "串口关闭异常：${e.message}")
        }
        // 重置状态
        serialPort = null
        inputStream = null
        outputStream = null
        bufferLen = 0
        Log.d(TAG, "串口已关闭")
    }

    /**
     * 检查连接状态
     */
    fun isSerialConnected() = isConnected
}