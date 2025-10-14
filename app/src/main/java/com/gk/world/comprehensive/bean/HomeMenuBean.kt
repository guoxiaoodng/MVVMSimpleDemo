package com.gk.world.comprehensive.bean

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

/**
 * 创建日期：2022/2/8 11:36
 * @author gxd
 * @version 1.0
 * 包名： com.gk.world.comprehensive.bean
 * 类说明：
 */

@Parcelize
data class HomeMenuItemBean(
    var id: String? = null,
    var title: String? = null,
    var icon: String? = null,
    var path: String? = null,
    var sort: Int = 0,
    @DrawableRes
    var drawable: Int = -1,
) : Parcelable
