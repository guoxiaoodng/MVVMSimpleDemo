package com.gk.world.resouce.dynamic.single

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/6
 *@time: 17:25
 *@description：每个item应该知道点击它去下一页  需要给下一页什么东西
 */
interface CommonListItemNextParamsInterface {
    /**
     * 获取下一页需要的参数
     * */
    fun getNextPageNeedParams(): MutableMap<String, *>
}