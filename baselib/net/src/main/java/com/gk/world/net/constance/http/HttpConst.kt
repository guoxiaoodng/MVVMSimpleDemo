package com.gk.world.net.constance.http

class HttpConst {
    //成功请求
    object SUCCESS {
        /**成功响应*/
        val HTTP_SUCCESS = 200..299

        /**
         * 200 响应 成功 并弹出 message
         */
        const val HTTP200 = 200

        /**
         * 201 响应 成功 弹出指定message
         */
        const val HTTP201 = 201

        /**
         * last
         */
        const val HTTP299 = 299
    }

    //失败请求
    object FAIL {
        /**失败响应*/
        val HTTP_FAIL = 400..499

        /**
         * 400 响应 失败 并弹出 message
         */
        const val HTTP400 = 400

        /**
         * 401 响应 失败 弹出指定message
         */
        const val HTTP401 = 401

        /**
         * token失效 去重新登录
         */
        const val HTTP403 = 403;

        /**
         * 验签失败
         */
        const val HTTP405 = 405;


        /**
         * last
         */
        const val HTTP499 = 499
    }

    //其它请求
    object OTHER {
        /**
         * 300 响应 其它 待定
         */
        const val HTTP300 = 300

        /**
         * 301 响应 其它 待定
         */
        const val HTTP301 = 301

        /**
         * 500 响应 其它 待定
         */
        const val HTTP500 = 500

        /**登录过期*/
        const val HTTP503 = 503
    }
}