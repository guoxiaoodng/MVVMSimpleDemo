package com.gk.world.resouce.extend

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/10
 *@time: 9:58
 *@description：
 */
class StringExtend {

    companion object {
        /**
         * 脱敏
         * */
        fun stringDesensitize(s: String): String {
            return when {
                s.length <= 5 -> {
                    s.substring(0, 1) + "**"
                }
                s.length in 6..11 -> {
                    s.substring(0, 3) + "****" + s.substring(s.length - 5, s.length - 1)
                }
                s.length >= 11 -> {
                    s.substring(0, 6) + "******" + s.substring(s.length - 4)
                }
                else -> {
                    s
                }
            }
        }
    }
}