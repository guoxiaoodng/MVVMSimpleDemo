package com.gk.world.net.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/4
 *@time: 10:53
 *@description： 全局用户对象
 */

@Serializable
@Parcelize
data class GlobalUserInfoBean(
    val birthDate: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val headerUrl: String = "",
    val userId: String? = null,
    var loginName: String? = null,
    val userName: String = "", 
    var token: String? = null,
    var password: String? = null,
    var position: String? = null,
    var departmentId: String? = null,
    var roles: List<RoleBean>? = null,
) : Parcelable

@Parcelize
data class GlobalUserDetailBean(
    val address: String? = null,
    val birthday: String? = null,
    val createId: String? = null,
    val createName: String? = null,
    val createTime: String? = null,
    val createTimestamp: String? = null,
    val deleted: Boolean? = null,
    val departmentId: String? = null,
    val education: String? = null,
    val email: String? = null,
    val enterpriseId: String? = null,
    val enterpriseName: String? = null,
    val gender: String? = null,
    val headerUrl: String? = null,
    val id: String? = null,
    val identityNumber: String? = null,
    val loginName: String? = null,
    val maritalStatus: String? = null,
    val nation: String? = null,
    val nationality: String? = null,
    val openId: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val position: String? = null,
    val remark: String? = null,
    val status: Boolean? = null,
    val updateId: String? = null,
    val updateName: String? = null,
    val updateTime: String? = null,
    val updateTimestamp: String? = null,
    val url: String? = null,
    val userName: String? = null,
    val userType: String? = null,
    val userTypeLabel: String? = null,
) : Parcelable

@Serializable
@Parcelize
data class RoleBean(
    val createId: String? = null,
    val createTime: String? = null,
    val createTimestamp: String? = null,
    val deleted: Boolean? = null,
    val id: String? = null,
    val memo: String? = null,
    val roleCode: String? = null,
    val roleName: String? = null,
    val status: Boolean? = null,
    val updateId: String? = null,
    val updateTime: String? = null,
    val updateTimestamp: String? = null,
) : Parcelable
