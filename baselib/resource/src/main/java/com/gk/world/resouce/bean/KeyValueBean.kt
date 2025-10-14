package com.gk.world.resouce.bean

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/15
 *@time: 9:42
 *@description：
 */
class KeyValueBean(
    private val k: String,
    private var v: String
) : KeyValueInterface {

    override fun getKey(): String = k

    override fun getValue(): String = v

    fun setValue(v: String) {
        this.v = v
    }
}