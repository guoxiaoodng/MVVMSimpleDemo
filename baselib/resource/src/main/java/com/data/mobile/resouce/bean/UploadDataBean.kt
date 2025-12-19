package com.data.mobile.resouce.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/1
 *@time: 18:27
 *@description：
 */
@Parcelize
data class UploadDataBean(
    val absoluteSavePath: String? = "",
    var accessUrl: String = "",
    val businessId: String? = "",
    val createId: String? = "",
    val createTime: String? = "",
    val createTimestamp: String? = "",
    val deleted: Boolean? = false,
    val id: String = "",
    val realName: String? = "",
    val relativeSavePath: String? = "",
    val saveName: String? = "",
    val status: Boolean? = false,
    val suffix: String? = "",
    val updateId: String? = "",
    val updateTime: String? = "",
    val updateTimestamp: String? = "",
    var url: String = "",
    val name: String? = "",
    val drawable: Int = -1,
    val add: Boolean = false,
) : KeyValueAddInterface, Parcelable {
    override fun getKey(): String {
        return id
    }

    override fun getValue(): String {
        return url
    }

    override fun add(): Boolean {
        return add
    }

    override fun drawable(): Int {
        return drawable
    }

}

@Parcelize
data class UploadFileBean(
    val id: String = "",
    val name: String = "",
    val url: String = "",
) : Parcelable, KeyValueInterface {
    override fun getKey(): String {
        return id
    }

    override fun getValue(): String {
        return url
    }
}