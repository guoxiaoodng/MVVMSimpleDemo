package com.gk.world.resouce.dynamic.kv

import androidx.annotation.IntDef

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/11
 *@time: 15:15
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
    KeyValueListEnum.IMG,
    KeyValueListEnum.VIDEO,
    KeyValueListEnum.RECORD,
    KeyValueListEnum.FILE,
    KeyValueListEnum.ALL,
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class KeyValueListEnum {
    companion object {


        /*图片*/
        const val IMG = 7

        /*视频*/
        const val VIDEO = 8

        /*音频*/
        const val RECORD = 9

        /*文件*/
        const val FILE = 10

        /*全部*/
        const val ALL = 11
    }
}