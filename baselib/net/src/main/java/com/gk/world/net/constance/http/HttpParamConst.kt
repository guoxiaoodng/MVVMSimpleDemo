package com.gk.world.net.constance.http

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/4
 *@time: 10:53
 *@description： 通用常量 - 针对接口参数
 */
class HttpParamConst {
    object ParamKey {
        /**
         * appId 应用表示 用户中台给的数据
         */
        const val APP_ID = "appId"

        /**
         * 用户token .net
         */
        const val TOKEN = "token"


        /**
         * 请求版本
         */
        const val VERSION = "VERSION"

        /**
         * 用户id .net
         */
        const val USER_ID = "userId"

        /**
         * 时间戳
         */
        const val TIMESTAMP = "timestamp"

        /**
         * 堆积字符
         */
        const val NONCE = "nonce"

        /**
         * 验签
         */
        const val SIGNATURE = "signature"

        /**
         * 区域id
         */
        const val DEPARTMENT_ID = "DepartmentId"
    }

    object ParamValue {
        /**
         * appId 用户中心提供的 应用id
         */
        const val APP_ID = "EDB48A7E-00B0-4D4D-BA8F-CA1662A00A36"

        /**
         *用户中心提供的 解码密钥
         */
        const val DECRYPT_SECRET = "BFDF52DA-A1B6-4F18-AD58-6C22B95E99F1"
    }
}