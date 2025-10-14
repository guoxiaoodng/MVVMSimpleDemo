package com.gk.world.resouce.arouter.guest

import android.os.Parcelable
import com.gk.world.resouce.dynamic.single.CommonMapListManager
import kotlinx.parcelize.Parcelize

/**
 * 访客对象
 * @author GK
 * @param behavior 访客行为 对象 默认是 read即：读取出数据查看
 * @param visitorRouter 访客路由地址
 * @param visitorMarker 访客的标识 特点 可以表明访客的状态
 *
 */
@Parcelize
class VisitorBean(
    @BehaviorEnum var behavior: Int = BehaviorEnum.R,
    var visitorRouter: String? = null,
) : Parcelable