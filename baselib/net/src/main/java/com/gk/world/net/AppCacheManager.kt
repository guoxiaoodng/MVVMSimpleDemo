package com.gk.world.net

import com.gk.world.net.bean.GlobalUserDetailBean
import com.gk.world.net.bean.GlobalUserInfoBean
import com.tencent.mmkv.MMKV

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/4
 *@time: 13:43
 *@description：应用缓存信息管理对象
 * 一个应用可能有多个 缓存块  这里 通过  MMKV.defaultMMKV(MMKV_MODE, CRYPT_KEY(这里区分))
 */
object AppCacheManager {


    /**
     * 登录后MMKV 地窖  key
     */
    private const val CRYPT_KEY_AFTER_LOGIN = "CRYPT_KEY_AFTER_LOGIN"

    /**
     * 登录前MMKV 地窖  key
     */
    private const val CRYPT_KEY_PRE_LOGIN = "CRYPT_KEY_PRE_LOGIN"

    /**
     * 登录后MMKV
     */
    fun getAftMmkv(): MMKV = MMKV.mmkvWithID(CRYPT_KEY_AFTER_LOGIN, MMKV.MULTI_PROCESS_MODE)

    /*获取登录对象*/
    fun getGlobalUserInfoBean(): GlobalUserInfoBean? = getAftMmkv().decodeParcelable(
        GlobalUserInfoBean::class.java.simpleName,
        GlobalUserInfoBean::class.java
    )

    fun getGlobalUserDetailBean(): GlobalUserDetailBean? = getAftMmkv().decodeParcelable(
        GlobalUserDetailBean::class.java.simpleName,
        GlobalUserDetailBean::class.java
    )

    /**
     * 登录后 多进程 MMKV
     */
    fun getAftMultiMmkv(): MMKV = MMKV.mmkvWithID(CRYPT_KEY_AFTER_LOGIN, MMKV.MULTI_PROCESS_MODE)

    /**
     * 登录前MMKV
     */
    fun getPreMmkv(): MMKV = MMKV.mmkvWithID(CRYPT_KEY_PRE_LOGIN)
}