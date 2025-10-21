package com.gxd.serialport.tools

import android.util.Log
import com.gxd.serialport.bean.MeasureResult
import com.gxd.serialport.constants.Rbp7000ProtocolConst.CHECKSUM_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DATA_LEN_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DEVICE_VERSION
import com.gxd.serialport.constants.Rbp7000ProtocolConst.DEVICE_VERSION_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.ERROR_CODE_MAP
import com.gxd.serialport.constants.Rbp7000ProtocolConst.HEADER_DEVICE_RESP
import com.gxd.serialport.constants.Rbp7000ProtocolConst.HEADER_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.HEADER_PC_SEND
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_CTRL_CONNECT
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_CTRL_ERROR
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_CTRL_MEAS_RESULT
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_CTRL_REAL_PRESSURE
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_CTRL_START
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_CTRL_STOP
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_QUERY_COMM_MODE
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_QUERY_DEVICE_CODE
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_QUERY_DEVICE_TIME
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_QUERY_SYS_CONFIG
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_SET_BAUD
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_SET_COMM_MODE
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_SET_FACTORY_RESET
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_SET_SYS_CONFIG
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_SET_TIME
import com.gxd.serialport.constants.Rbp7000ProtocolConst.SUB_TYPE_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.TYPE_CONTROL
import com.gxd.serialport.constants.Rbp7000ProtocolConst.TYPE_ID_LENGTH
import com.gxd.serialport.constants.Rbp7000ProtocolConst.TYPE_QUERY
import com.gxd.serialport.constants.Rbp7000ProtocolConst.TYPE_SETTING
import com.gxd.serialport.parser.DataParser
import kotlin.experimental.xor

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2025/10/15
 *@time: 12:30
 *@description：协议帧组装/解析工具（严格遵循文档2.1-2.7、3-5章节规则）
 */
object ProtocolFrameTool {
    private const val TAG = "ProtocolFrameTool"

    /**
     * 组装PC端发送帧（文档2.1-2.7）
     * @param type 类型标识（控制/设置/查询）
     * @param subType 类型子码
     * @param dataContent 数据内容（无数据传空数组）
     * @return 完整发送帧：前导码 + 设备版本 + 数据长度 + 数据区 + 校验码
     */
    fun buildSendFrame(type: Byte, subType: Byte, dataContent: ByteArray = byteArrayOf()): ByteArray {
        // 1. 数据区：类型标识 + 类型子码 + 数据内容（文档2.3：数据长度为此区域总长度）
        val dataArea = byteArrayOf(type, subType) + dataContent
        val dataLen = dataArea.size.toByte() // 数据长度字段值

        // 2. 待校验数据：设备版本 + 数据长度 + 数据区（文档2.7：校验码=除前导码、校验码外所有字节异或）
        val dataToCheck = byteArrayOf(DEVICE_VERSION, dataLen) + dataArea
        val checksum = calculateChecksum(dataToCheck)

        // 3. 完整帧：前导码 + 待校验数据 + 校验码
        return HEADER_PC_SEND + dataToCheck + checksum
    }

    /**
     * 计算校验码（文档2.7：按位异或）
     */
    private fun calculateChecksum(data: ByteArray): Byte {
        var checksum = 0x00.toByte()
        for (b in data) {
            checksum = checksum xor b
        }
        return checksum
    }

    /**
     * 解析设备接收帧（校验+数据分发）
     * @param frame 完整接收帧
     * @param listener 数据回调
     */
    fun parseReceiveFrame(frame: ByteArray, listener: SerialDataListener) {
        Log.d(TAG, "接收帧：${DataParser.bytesToHexString(frame)}")
        val frameLen = frame.size

        // 1. 校验前导码（必须为0xAA,0x80，文档3.1.1）
        if (frameLen < HEADER_LENGTH ||
                frame[0] != HEADER_DEVICE_RESP[0] ||
                frame[1] != HEADER_DEVICE_RESP[1]) {
            Log.e(TAG, "前导码错误，丢弃帧")
            return
        }

        // 2. 校验帧最小长度（前导码+设备版本+数据长度+校验码=5字节）
        val minFrameLen = HEADER_LENGTH + DEVICE_VERSION_LENGTH + DATA_LEN_LENGTH + CHECKSUM_LENGTH
        if (frameLen < minFrameLen) {
            Log.e(TAG, "帧长度不足（最小${minFrameLen}字节），当前${frameLen}字节")
            return
        }

        // 3. 校验设备版本（必须为0x03，文档1-14）
        val deviceVer = frame[HEADER_LENGTH]
        if (deviceVer != DEVICE_VERSION) {
            Log.e(TAG, "设备版本错误（需0x03），当前0x${deviceVer.toInt() and 0xFF}")
            listener.onError("设备版本不匹配")
            return
        }

        // 4. 提取数据长度和数据区
        val dataLen = frame[HEADER_LENGTH + DEVICE_VERSION_LENGTH].toInt() and 0xFF
        val dataAreaStart = HEADER_LENGTH + DEVICE_VERSION_LENGTH + DATA_LEN_LENGTH
        val dataAreaEnd = dataAreaStart + dataLen
        if (dataAreaEnd + CHECKSUM_LENGTH > frameLen) {
            Log.e(TAG, "数据区长度不足（需${dataLen}字节），当前帧剩余${frameLen - dataAreaStart}字节")
            return
        }
        val dataArea = frame.copyOfRange(dataAreaStart, dataAreaEnd)

        // 5. 校验校验码（文档2.7）
        val receivedChecksum = frame[dataAreaEnd]
        val dataToCheck = byteArrayOf(DEVICE_VERSION, dataLen.toByte()) + dataArea
        val calculatedChecksum = calculateChecksum(dataToCheck)
        if (receivedChecksum != calculatedChecksum) {
            Log.e(TAG, "校验码错误：接收0x${receivedChecksum.toInt() and 0xFF}，计算0x${calculatedChecksum.toInt() and 0xFF}")
            return
        }

        // 6. 提取类型标识和子码（数据区前2字节）
        if (dataArea.size < TYPE_ID_LENGTH + SUB_TYPE_LENGTH) {
            Log.e(TAG, "数据区长度不足，无法提取类型标识（需至少2字节）")
            return
        }
        val type = dataArea[0]
        val subType = dataArea[1]
        val dataContent = if (dataArea.size > 2) dataArea.copyOfRange(2, dataArea.size) else byteArrayOf()

        // 7. 按类型分发数据
        dispatchParsedData(type, subType, dataContent, listener)
    }

    /**
     * 分发解析后的数据（对应文档3-5章节指令）
     */
    private fun dispatchParsedData(
            type: Byte,
            subType: Byte,
            dataContent: ByteArray,
            listener: SerialDataListener
    ) {
        when (type) {
            TYPE_CONTROL -> parseControlData(subType, dataContent, listener)
            TYPE_SETTING -> parseSettingData(subType, dataContent, listener)
            TYPE_QUERY -> parseQueryData(subType, dataContent, listener)
            else -> Log.w(TAG, "未知类型标识：0x${type.toInt() and 0xFF}")
        }
    }

    /**
     * 解析控制类数据（文档3）
     */
    private fun parseControlData(
            subType: Byte,
            dataContent: ByteArray,
            listener: SerialDataListener
    ) {
        when (subType) {
            SUB_CTRL_CONNECT -> {
                Log.d(TAG, "设备连接应答成功")
                listener.onConnected(true)
            }
            SUB_CTRL_START -> Log.d(TAG, "设备启动测量应答成功")
            SUB_CTRL_STOP -> Log.d(TAG, "设备停止测量应答成功")
            SUB_CTRL_REAL_PRESSURE -> {
                // 文档3.4+表1：实时压力=高8位*256+低8位（数据内容2字节）
                if (dataContent.size >= 2) {
                    val pressure = (dataContent[0].toInt() and 0xFF) shl 8 or (dataContent[1].toInt() and 0xFF)
                    listener.onRealPressure(pressure)
                } else {
                    Log.e(TAG, "实时压力数据长度不足（需2字节）")
                }
            }
            SUB_CTRL_MEAS_RESULT -> {
                // 文档3.7 FUN1：数据内容=用户标识(1)+时间(6)+血压(6)（共13字节）
                if (dataContent.size >= 13) {
                    val userFlag = dataContent[0]
                    val timeData = dataContent.copyOfRange(1, 7)
                    val bloodData = dataContent.copyOfRange(7, 13)

                    val measureTime = DataParser.parseTime(timeData)
                    val bloodPressure = DataParser.parseBloodPressure(bloodData)
                    listener.onMeasureResult(MeasureResult(userFlag, measureTime, bloodPressure))
                } else {
                    Log.e(TAG, "测量结果数据长度不足（需13字节），当前${dataContent.size}字节")
                }
            }
            SUB_CTRL_ERROR -> {
                // 文档3.5+表2：数据内容1字节错误码
                if (dataContent.isNotEmpty()) {
                    val errorCode = dataContent[0]
                    val errorMsg = ERROR_CODE_MAP[errorCode] ?: "未知错误：0x${errorCode.toInt() and 0xFF}"
                    listener.onError(errorMsg)
                } else {
                    Log.e(TAG, "错误码数据长度不足（需1字节）")
                }
            }
            else -> Log.w(TAG, "未知控制类子码：0x${subType.toInt() and 0xFF}")
        }
    }

    /**
     * 解析设置类数据（文档4，仅处理设置成功应答）
     */
    private fun parseSettingData(
            subType: Byte,
            dataContent: ByteArray,
            listener: SerialDataListener
    ) {
        // 文档4.5：设置成功应答→数据内容为0x00，子码对应设置项
        if (dataContent.size >= 1 && dataContent[0] == 0x00.toByte()) {
            listener.onSettingSuccess(subType)
            val settingDesc = when (subType) {
                SUB_SET_BAUD -> "波特率"
                SUB_SET_COMM_MODE -> "通讯模式"
                SUB_SET_TIME -> "设备时间"
                SUB_SET_SYS_CONFIG -> "系统配置"
                SUB_SET_FACTORY_RESET -> "恢复出厂设置"
                else -> "未知设置项"
            }
            Log.d(TAG, "${settingDesc}设置成功")
        }
    }

    /**
     * 解析查询类数据（文档5）
     */
    private fun parseQueryData(
            subType: Byte,
            dataContent: ByteArray,
            listener: SerialDataListener
    ) {
        when (subType) {
            SUB_QUERY_DEVICE_CODE -> {
                // 文档5.1.1+表11：数据内容7字节设备编码
                val deviceCode = DataParser.parseDeviceCode(dataContent)
                listener.onDeviceCode(deviceCode)
            }
            SUB_QUERY_DEVICE_TIME -> {
                // 文档5.2.1+表4：数据内容6字节时间
                val deviceTime = DataParser.parseTime(dataContent)
                listener.onDeviceTime(deviceTime)
            }
            SUB_QUERY_SYS_CONFIG -> {
                // 文档5.3.1+表9：数据内容6字节系统配置（单位/语音/MAP/休眠时间）
                if (dataContent.size >= 6) {
                    listener.onSysConfig(DataParser.bytesToHexString(dataContent))
                } else {
                    Log.e(TAG, "系统配置数据长度不足（需6字节）")
                }
            }
            SUB_QUERY_COMM_MODE -> {
                // 文档5.4.1+表10：数据内容7字节通讯模式
                if (dataContent.size >= 7) {
                    listener.onCommMode(DataParser.bytesToHexString(dataContent))
                } else {
                    Log.e(TAG, "通讯模式数据长度不足（需7字节）")
                }
            }
            else -> Log.w(TAG, "未知查询类子码：0x${subType.toInt() and 0xFF}")
        }
    }
}

/**
 * 串口数据回调接口
 */
interface SerialDataListener {
    fun onConnected(success: Boolean) // 连接结果
    fun onRealPressure(pressure: Int) // 实时压力（文档3.4）
    fun onMeasureResult(result: MeasureResult) // 测量结果（文档3.7）
    fun onError(errorMsg: String) // 错误（文档3.5）
    fun onDeviceCode(deviceCode: String) // 设备编码（文档5.1）
    fun onDeviceTime(deviceTime: String) // 设备时间（文档5.2）
    fun onSysConfig(config: String) // 系统配置（文档5.3）
    fun onCommMode(mode: String) // 通讯模式（文档5.4）
    fun onSettingSuccess(subType: Byte) // 设置成功（文档4.5）
}