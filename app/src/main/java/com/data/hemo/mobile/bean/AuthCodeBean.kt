package com.data.hemo.mobile.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/17
 *@time: 13:48
 *@description：
 */
@Parcelize
data class AuthCodeBean(
    val img: String,
    val uuid: String
):Parcelable