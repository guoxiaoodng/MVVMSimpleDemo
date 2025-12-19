package com.data.hemo.mobile.bean

/**
 * 创建日期：2022/3/8 10:04
 * @author gxd
 * @version 1.0
 * 包名： com.data.hemo.mobile.bean
 * 类说明：
 */
data class AppVersionBean(
    val defaultInstall: Boolean,
    val forceUpdate: Boolean,
    val installPackageName: String,
    val packagePath: String,
    val versionName: String,
    val versionNumber: String
)