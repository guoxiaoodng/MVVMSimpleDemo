package com.gk.world.net.bean

import kotlinx.serialization.Serializable

@Serializable
open class BaseBean<T>(
    var message: String = "net no message content",
    var code: Int = 0,
    var data: T? = null
)