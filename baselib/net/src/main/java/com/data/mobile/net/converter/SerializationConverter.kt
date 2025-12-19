@file:Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")

package com.data.mobile.net.converter

import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import com.drake.net.request.kType
import com.data.mobile.net.constance.http.HttpConst
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.reflect.KType

class SerializationConverter(
    val success: Int = HttpConst.SUCCESS.HTTP200,
    val data: String = "data",
    val code: String = "code",
    val message: String = "message",
) : NetConverter {

    private val jsonDecoder = Json {
        ignoreUnknownKeys = true // JSON和数据模型字段可以不匹配
        coerceInputValues = true // 如果JSON字段是Null则使用默认值
    }

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        try {
            return NetConverter.onConvert<R>(succeed, response)
        } catch (e: ConvertException) {
            val code = response.code
            when {
                code in 200..299 -> { // 请求成功 这里指向http的响应码
                    val bodyString = response.body?.string() ?: return null
                    val kType = response.request.kType
                        ?: throw ConvertException(response, "Request does not contain KType")
                    return try {
                        val json = JSONObject(bodyString) // 获取JSON中后端定义的错误码和错误信息
                        when {
                            //这里处理的是 自己服务返回的code
                            json.getInt(this.code) == success || json.getInt(this.code) == HttpConst.SUCCESS.HTTP201 -> {
                                bodyString.parseBody<R>(kType)
                            }
                            json.getInt(this.code) == HttpConst.FAIL.HTTP403 -> {//token 失效 去登录页
                                throw ResponseException(response, "用户在另一台设备登录，请重新登录")
                            }
                            else -> { // 错误码匹配失败, 开始写入错误异常
                                val errorMessage = json.optString(
                                    message,
                                    NetConfig.app.getString(com.drake.net.R.string.no_error_message)
                                )
                                throw ResponseException(response, errorMessage)
                            }
                        }
                    } catch (e: JSONException) { // 固定格式JSON分析失败直接解析JSON
                        bodyString.parseBody<R>(kType)
                    }
                }
                code in 400..499 -> throw RequestParamsException(
                    response,
                    code.toString()
                ) // 请求参数错误
                code >= 500 -> throw ServerResponseException(response, code.toString()) // 服务器异常错误
                else -> throw ConvertException(response)
            }
        }
    }

    fun <R> String.parseBody(succeed: KType): R? {
        return jsonDecoder.decodeFromString(Json.serializersModule.serializer(succeed), this) as R
    }
}