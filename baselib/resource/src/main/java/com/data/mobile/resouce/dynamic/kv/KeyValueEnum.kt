package com.data.mobile.resouce.dynamic.kv

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
    KeyValueEnum.ANY,
    KeyValueEnum.PHONE,
    KeyValueEnum.IDENTITY,
    KeyValueEnum.DATE,
    KeyValueEnum.SELECT_RES,
    KeyValueEnum.LOCAL,
)
@Retention(AnnotationRetention.SOURCE)
annotation class KeyValueEnum {
    companion object {

        /*全部*/
        const val ANY = 0

        /*手机*/
        const val PHONE = 1

        /*身份证*/
        const val IDENTITY = 2

        /*日期*/
        const val DATE = 3

        /*选择资源*/
        const val SELECT_RES = 4

        /*定位坐标*/
        const val LOCAL = 5
    }
}