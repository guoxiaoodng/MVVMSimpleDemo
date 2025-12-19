// NetworkUtils.kt
package com.data.hemo.mobile.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.text.format.Formatter
import java.net.NetworkInterface

object NetworkUtils {

    fun getLocalIpAddress(context: Context): String? {
        return try {
            // 方法1：通过WifiManager获取（需要WIFI连接）
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            val wifiInfo = wifiManager?.connectionInfo
            if (wifiInfo != null && wifiInfo.ipAddress != 0) {
                Formatter.formatIpAddress(wifiInfo.ipAddress)
            } else {
                // 方法2：遍历网络接口
                getLocalIpAddressFromInterfaces()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getLocalIpAddressFromInterfaces(): String? {
        return try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val intf = interfaces.nextElement()
                val addresses = intf.inetAddresses
                while (addresses.hasMoreElements()) {
                    val addr = addresses.nextElement()
                    if (!addr.isLoopbackAddress && addr.hostAddress?.contains(':') == false) {
                        // 过滤掉IPv6和非局域网地址
                        val ip = addr.hostAddress ?: continue
                        if (ip.startsWith("192.168.") ||
                            ip.startsWith("10.") ||
                            ip.startsWith("172.") && ip.split('.')[1].toInt() in 16..31) {
                            return ip
                        }
                    }
                }
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    fun getNetworkInfo(context: Context): String {
        return buildString {
            append("网络状态: ")
            if (isWifiConnected(context)) {
                val ip = getLocalIpAddress(context)
                append("已连接WIFI")
                if (ip != null) {
                    append(" (IP: $ip)")
                }
            } else {
                append("未连接WIFI，请确保两台设备在同一网络")
            }
        }
    }
}