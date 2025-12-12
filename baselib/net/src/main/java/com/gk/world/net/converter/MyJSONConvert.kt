package com.gk.world.net.converter

import android.os.Parcelable
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.net.NetConfig
import com.drake.net.convert.NetConverter
import com.drake.net.exception.ConvertException
import com.drake.net.exception.RequestParamsException
import com.drake.net.exception.ResponseException
import com.drake.net.exception.ServerResponseException
import com.gk.world.net.constance.http.HttpConst
import com.google.gson.Gson
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/5
 *@time: 10:57
 *@description：
 */
abstract class MyJSONConvert(
    val code: String = "code",
    val message: String = "message",
    val data: String = "data",
) : NetConverter, Parcelable{

    override fun <R> onConvert(succeed: Type, response: Response): R? {
        /* try {
             return NetConverter.onConvert<R>(succeed, response)
         } catch (e: ConvertException) {*/
        val code = response.code
        when {
            code in HttpConst.SUCCESS.HTTP_SUCCESS -> { // 请求成功 这里指向http的响应码
                val bodyString = response.body?.string() ?: return null
                return try {
                    val json = JSONObject(bodyString) // 获取JSON中后端定义的错误码和错误信息
                    when {
                        //这里处理的是 自己服务返回的code
                        json.getInt(this.code) == HttpConst.SUCCESS.HTTP200 || json.getInt(this.code) == HttpConst.SUCCESS.HTTP201 -> {
                            bodyString.parseBody<R>(succeed)
                        }
                        /*json.getInt(this.code) == HttpConst.OTHER.HTTP503 ||//token 无啦 去登录页
                                json.getInt(this.code) == HttpConst.FAIL.HTTP401 -> {//token 无啦 去登录页
                            ARouter.getInstance().build("/main/LoginActivity").navigation()
                            val errorMessage = json.optString(
                                message,
                                "用户已过期，请重新登录"
                            )
                            throw ResponseException(response, errorMessage)
                        }*/
                        else -> { // 错误码匹配失败, 开始写入错误异常
                            val errorMessage = json.optString(
                                message,
                                NetConfig.app.getString(com.drake.net.R.string.no_error_message)
                            )
                            throw ResponseException(response, errorMessage)
                        }
                    }
                } catch (e: JSONException) { // 固定格式JSON分析失败直接解析JSON
                    Gson().toJson(bodyString).parseBody<R>(succeed)
                }
            }
            code in HttpConst.FAIL.HTTP_FAIL -> throw RequestParamsException(
                response,
                code.toString()
            ) // 请求参数错误
            code >= HttpConst.OTHER.HTTP500 -> throw ServerResponseException(
                response,
                code.toString()
            ) // 服务器异常错误
            else -> throw ConvertException(response)
        }
//        }
    }

    /**
     * 反序列化JSON
     *
     * @param succeed JSON对象的类型
     * @receiver 原始字符串
     */
    abstract fun <R> String.parseBody(succeed: Type): R?
}