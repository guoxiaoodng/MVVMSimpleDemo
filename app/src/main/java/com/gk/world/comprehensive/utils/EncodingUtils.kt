// EncodingUtils.kt
package com.gk.world.comprehensive.utils

import android.util.Base64
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object EncodingUtils {

    /**
     * 智能解码字符串，尝试多种编码
     */
    fun smartDecode(input: ByteArray): String {
        return try {
            // 优先尝试UTF-8
            String(input, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            try {
                // 尝试GBK
                String(input, Charset.forName("GBK"))
            } catch (e: Exception) {
                try {
                    // 尝试ISO-8859-1
                    String(input, StandardCharsets.ISO_8859_1)
                } catch (e: Exception) {
                    // 最后尝试系统默认编码
                    String(input, Charset.defaultCharset())
                }
            }
        }
    }

    /**
     * 检测并修复乱码
     */
    fun fixChineseEncoding(input: String): String {
        if (input.isBlank()) return input

        // 常见乱码模式检测
        val patterns = mapOf(
            "ã" to "ä",  // UTF-8被误读为Latin-1
            "Ã" to "Ä",
            "â" to "â",
            "Â" to "Â",
            "ä" to "ä",
            "Ä" to "Ä"
        )

        var result = input
        patterns.forEach { (wrong, correct) ->
            result = result.replace(wrong, correct)
        }

        // 如果仍然有乱码，尝试编码转换
        if (containsMojibake(result)) {
            return try {
                // 假设是UTF-8被误读为ISO-8859-1，再转回UTF-8
                val bytes = result.toByteArray(StandardCharsets.ISO_8859_1)
                String(bytes, StandardCharsets.UTF_8)
            } catch (e: Exception) {
                result
            }
        }

        return result
    }

    /**
     * 检测是否包含常见乱码字符
     */
    private fun containsMojibake(input: String): Boolean {
        // 检测常见乱码模式
        val mojibakePatterns = listOf(
            "Ã¦", "Ã¥", "Ã¸", "Ã†", "Ã…", "Ã˜",  // 北欧字符乱码
            "â€", "â€¢", "â€¦", "â€\"",  // 标点乱码
            "Â", "Â£", "Â©", "Â®",  // 货币和符号乱码
            "ä»", "å…", "çš„", "æ˜¯"  // 中文字符被拆分
        )

        return mojibakePatterns.any { input.contains(it) }
    }

    /**
     * Base64编码字符串
     */
    fun encodeToBase64(input: String): String {
        return Base64.encodeToString(
            input.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP
        )
    }

    /**
     * Base64解码字符串
     */
    fun decodeFromBase64(input: String): String {
        return try {
            val decodedBytes = Base64.decode(input, Base64.DEFAULT)
            String(decodedBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            input
        }
    }

    /**
     * 获取字符串的编码猜测
     */
    fun guessEncoding(input: ByteArray): String {
        return try {
            // 尝试UTF-8 BOM
            if (input.size >= 3 &&
                input[0].toInt() == 0xEF &&
                input[1].toInt() == 0xBB &&
                input[2].toInt() == 0xBF) {
                return "UTF-8 with BOM"
            }

            // 尝试UTF-16 BOM
            if (input.size >= 2) {
                if (input[0].toInt() == 0xFE && input[1].toInt() == 0xFF) {
                    return "UTF-16BE"
                }
                if (input[0].toInt() == 0xFF && input[1].toInt() == 0xFE) {
                    return "UTF-16LE"
                }
            }

            // 通过字符分布猜测
            val likelyUtf8 = String(input, StandardCharsets.UTF_8)
            val likelyGbk = String(input, Charset.forName("GBK"))

            // 统计有效字符比例
            val utf8Valid = likelyUtf8.count { it.code in 32..126 || it.code > 127 }
            val gbkValid = likelyGbk.count { it.code in 32..126 || it.code > 127 }

            when {
                utf8Valid > gbkValid * 1.2 -> "UTF-8"
                gbkValid > utf8Valid * 1.2 -> "GBK"
                else -> "Unknown"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}