package com.data.mobile.resouce.dynamic.kv

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/16
 *@time: 10:24
 *@description： KeyValueEnum 对用的点击事件
 * 参考[KeyValueEnum]
 */
interface KeyValueClickListener {
    fun addAnyClick(isEdit: Boolean) {}
    fun addPhoneClick(isEdit: Boolean) {}
    fun addIdentityClick(isEdit: Boolean) {}
    fun addDateClick(isEdit: Boolean) {}
    fun addSelectResClick(isEdit: Boolean) {}
    fun addLocalClick(isEdit: Boolean) {}
}