package com.tecent.tecentx5.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/14
 *@time: 10:54
 *@description：
 */
@Parcelize
class MediaBean(var url: String, var title: String = "媒体资源") : Parcelable {
}