package com.gk.world.comprehensive.bean

import android.os.Parcelable
import com.gk.world.resouce.dynamic.single.CommonListItemNextParamsInterface
import kotlinx.parcelize.Parcelize

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/7
 *@time: 15:18
 *@description：
 */
@Parcelize
data class UserInfoBean(
    var name: String? = null,
    var id: Int = 0,
    var age: Int = 0,
    var height: Int = 0,
) : Parcelable