package com.gk.world.resouce.dynamic.single

import androidx.annotation.DrawableRes
import com.drake.net.convert.NetConverter
import com.gk.world.net.constance.http.HttpWay
import com.gk.world.net.converter.MyJSONConvert
import com.gk.world.resouce.arouter.guest.BehaviorEnum
import kotlinx.parcelize.RawValue
import org.jetbrains.annotations.NotNull

class CommonMapListManager(
    /**行为类型 [BehaviorEnum]*/
    @BehaviorEnum var behaviorType: Int = BehaviorEnum.R,

    /**布局ID*/
    @NotNull
    var itemLayout: Int = 0,

    /**请求参数*/
    var requestMap: MutableMap<String, Any> = mutableMapOf(),

    /**分割布局 优先选择这个*/
    @DrawableRes
    var divider: Int = -1,

    /**分割宽度 次选*/
    var dividerWidth: Int = 10,

    /**初始化 第一页的页面 一般是 1 偶尔是0*/
    var pageInit: Int = 1,

    /**页码是个特殊值 要本地递增 所以单独传递*/
    var pageParamKey: String = "page",

    /**请求的地址*/
    @NotNull
    var requestUrl: String,

    /**请求方式*/
    @HttpWay
    var requestWay: Int = HttpWay.GET,

    /**下一页的路由地址*/
    var nextPageRouter: String,

    /**访客的路由地址*/
    var visitorRouter: String,


    /**解析器*/
    var converter: NetConverter? = null,

    /**事件触发器 - 通过这个更新当前页面  不用visitorRouter 是因为visitorRouter是必传字段 但却不一定要 回调
     * 设置了 eventTriggers才表明要回调 */
    var eventTriggers: String? = null,
    /**
     * 发送事件 下面时事件目标 在完成任务时有可能需要更新一些不相关的页面 所以需要这个字段
     * */
    var sendTargetEvents: Array<String>? = Array(0) { "" },
    /**自动刷新*/
    var refresh: Boolean = true,
) {

    /**点击item后 返回/的构造*/
    constructor(
        @BehaviorEnum
        behaviorType: Int = BehaviorEnum.RG,
        @NotNull
        itemLayout: Int,
        requestMap: MutableMap<String, @RawValue Any> = mutableMapOf(),
        @DrawableRes
        divider: Int = -1,
        dividerWidth: Int = 10,
        pageInit: Int = 1,
        @NotNull
        pageParamKey: String,
        @NotNull
        requestUrl: String,
        @HttpWay
        requestWay: Int = HttpWay.GET,
        visitorRouter: String,
        @NotNull
        converter: MyJSONConvert? = null,
        eventTriggers: String? = null,
        sendTargetEvents: Array<String>? = Array(0) { "" },
        refresh: Boolean = true,
    ) : this(
        behaviorType,
        itemLayout,
        requestMap,
        divider,
        dividerWidth,
        pageInit,
        pageParamKey,
        requestUrl,
        requestWay,
        "",
        visitorRouter,
        converter,
        eventTriggers,
        sendTargetEvents,
        refresh
    )
}