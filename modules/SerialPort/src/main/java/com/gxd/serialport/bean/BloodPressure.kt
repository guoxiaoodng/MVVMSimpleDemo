package com.gxd.serialport.bean

/**
 * 血压数据实体（文档表5）
 * @param sys 收缩压（mmHg）
 * @param dia 舒张压（mmHg）
 * @param pul 脉搏（次/分钟）
 */
data class BloodPressure(
        val sys: Int,
        val dia: Int,
        val pul: Int
) {
    override fun toString(): String {
        return "收缩压：${sys}mmHg，舒张压：${dia}mmHg，脉搏：${pul}次/分钟"
    }
}

/**
 * 测量结果实体（文档3.7 FUN1）
 * @param userFlag 用户标识（文档3.7）
 * @param measureTime 测量时间（yyyy-MM-dd HH:mm:ss）
 * @param bloodPressure 血压数据
 */
data class MeasureResult(
        val userFlag: Byte,
        val measureTime: String,
        val bloodPressure: BloodPressure
) {
    override fun toString(): String {
        return "用户标识：${userFlag.toInt()}，测量时间：${measureTime}，${bloodPressure}"
    }
}