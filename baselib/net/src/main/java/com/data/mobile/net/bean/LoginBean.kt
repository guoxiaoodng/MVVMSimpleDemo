package com.data.mobile.net.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/5
 *@time: 9:51
 *@description：
 */
@Parcelize
data class LoginBean(val loginName: String?, var loginPas: String?) : Parcelable