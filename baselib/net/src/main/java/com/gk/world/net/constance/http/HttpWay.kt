package com.gk.world.net.constance.http

import androidx.annotation.IntDef


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/18
 *@time: 9:37
 *@description：
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.LOCAL_VARIABLE
)
@MustBeDocumented
@IntDef(
    HttpWay.GET,
    HttpWay.POST_URLENCODED,
    HttpWay.POST_FORM_DATA,
    HttpWay.POST_JSON,
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class HttpWay {
    companion object {
        const val GET = 1

        /**xx-www-form-urlencoded*/
        const val POST_URLENCODED = 2

        /**multipart/form-data*/
        const val POST_FORM_DATA = 3

        /**application/json*/
        const val POST_JSON = 4
    }
}