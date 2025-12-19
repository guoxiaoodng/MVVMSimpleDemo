package com.data.mobile.resouce.dynamic.single

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/9/15
 *@time: 15:11
 *@description：
 */
@Parcelize
class KeyValueBean(val key: String, val value: @RawValue Any) :Parcelable{
}