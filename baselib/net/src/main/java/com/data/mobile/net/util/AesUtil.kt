package com.data.mobile.net.util

import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/4
 *@time: 8:17
 *@description：
 */
private const val secret = "2d8kb41xWH3V9bPggr+w6w=="
private const val iv = "BjNzhiZDctOGMxOS"
fun String.encrypt(): String {
    val content: ByteArray = this.toByteArray()
    val secretKeySpec = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "AES")
    val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") //"算法/模式/补码方式"
    val ivParameterSpec =
        IvParameterSpec(iv.toByteArray(Charsets.UTF_8)) // 使用 CBC 模式，需要一个向量 iv, 可增加加密算法的强度
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
    return Base64.getEncoder().encodeToString(cipher.doFinal(content))
}

fun String.decryptStr(): String {
    val contentDecByBase64: ByteArray = Base64.getDecoder().decode(this)
    val secretKeySpec = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") //"算法/模式/补码方式"
    val ivParameterSpec = IvParameterSpec(iv.toByteArray(Charsets.UTF_8))
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
    return String(cipher.doFinal(contentDecByBase64), Charset.defaultCharset())
}
