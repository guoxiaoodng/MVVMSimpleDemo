package com.gxd.serialport.constants

import android.serialport.SerialPort

object Rbp7000ProtocolConst {
    // 1. 串口基础配置（文档1-6、1-7）
    const val DATA_BITS = 8// 8bit数据位
    const val STOP_BITS = 1 // 1bit停止位
    const val PARITY = 0 // 无校验位
    const val FLOW_CONTROL = 0 // 无流控
    const val DEFAULT_BAUD_RATE = 9600 // 默认波特率（文档1-6）
    // 波特率映射（文档表8：0x01~0x06对应2400/4800/9600/19200/115200/38400）
    val BAUD_RATE_MAP = mapOf(
            0x01 to 2400,
            0x02 to 4800,
            0x03 to 9600,
            0x04 to 19200,
            0x05 to 115200,
            0x06 to 38400
    )

    // 2. 协议帧结构（文档2.1-2.7）
    val HEADER_PC_SEND = byteArrayOf(0xCC.toByte(), 0x80.toByte()) // PC端发送前导码
    val HEADER_DEVICE_RESP = byteArrayOf(0xAA.toByte(), 0x80.toByte()) // 设备应答/主动发送前导码
    const val DEVICE_VERSION = 0x03.toByte() // 设备版本（串口通讯固定0x03，文档1-14）
    const val HEADER_LENGTH = 2 // 前导码长度（2字节）
    const val DEVICE_VERSION_LENGTH = 1 // 设备版本长度（1字节）
    const val DATA_LEN_LENGTH = 1 // 数据长度字段长度（1字节）
    const val TYPE_ID_LENGTH = 1 // 类型标识长度（1字节）
    const val SUB_TYPE_LENGTH = 1 // 类型子码长度（1字节）
    const val CHECKSUM_LENGTH = 1 // 校验码长度（1字节）

    // 3. 类型标识（文档2.4、3-5章节）
    const val TYPE_CONTROL = 0x01.toByte() // 控制类指令（文档3）
    const val TYPE_SETTING = 0x02.toByte() // 设置类指令（文档4）
    const val TYPE_QUERY = 0x03.toByte() // 查询类指令（文档5）

    // 4. 控制类子码（文档3）
    const val SUB_CTRL_CONNECT = 0x01.toByte() // 连接指令（3.1）
    const val SUB_CTRL_START = 0x02.toByte() // 启动测量（3.2）
    const val SUB_CTRL_STOP = 0x03.toByte() // 停止测量（3.3）
    const val SUB_CTRL_REAL_PRESSURE = 0x05.toByte() // 实时压力（3.4）
    const val SUB_CTRL_MEAS_RESULT = 0x06.toByte() // 测量结果（3.7）
    const val SUB_CTRL_ERROR = 0x07.toByte() // 错误代码（3.5）
    const val SUB_CTRL_KEY_OP = 0x08.toByte() // 按键操作（3.6）
    const val SUB_CTRL_ELBOW_KEY = 0x09.toByte() // 手肘按键（3.7）

    // 5. 设置类子码（文档4）
    const val SUB_SET_BAUD = 0x01.toByte() // 波特率（4.1）
    const val SUB_SET_COMM_MODE = 0x02.toByte() // 通讯模式（4.2）
    const val SUB_SET_TIME = 0x03.toByte() // 设备时间（4.3）
    const val SUB_SET_SYS_CONFIG = 0x04.toByte() // 单位/语音/MAP（4.4）
    const val SUB_SET_FACTORY_RESET = 0x05.toByte() // 恢复出厂（4.6）

    // 6. 查询类子码（文档5）
    const val SUB_QUERY_DEVICE_CODE = 0x01.toByte() // 设备编码（5.1）
    const val SUB_QUERY_DEVICE_TIME = 0x02.toByte() // 设备时间（5.2）
    const val SUB_QUERY_SYS_CONFIG = 0x03.toByte() // 系统配置（5.3）
    const val SUB_QUERY_COMM_MODE = 0x04.toByte() // 通讯模式（5.4）

    // 7. 错误码（文档表2）
    val ERROR_CODE_MAP = mapOf(
            0x01.toByte() to "压力保护",
            0x02.toByte() to "信号检测错误或上游气囊漏气，请重新测量",
            0x05.toByte() to "信号检测错误或下游气囊漏气，请重新测量",
            0x06.toByte() to "信号检测错误或传感器错误，请重新测量",
            0x09.toByte() to "放气异常",
            0x0F.toByte() to "系统漏气",
            0x10.toByte() to "电机错误"
    )
}