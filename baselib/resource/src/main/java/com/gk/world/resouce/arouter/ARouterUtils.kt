package com.gk.world.resouce.arouter

import android.app.Activity
import android.os.Parcelable
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.arouter.guest.VisitorBean
import com.hjq.toast.ToastUtils
import java.io.Serializable
import java.util.*

object ARouterUtils {
    /**
     * 常用的跳转 用户行为 到进入页面在处理
     * @param path 路由
     * I 访客携带的参数信息
     * I 访客
     */
    fun routerParcelable(path: String, visitor: VisitorBean?, data: Parcelable) {
        ARouter.getInstance().build(path)
            .withParcelable(ARouterConstance.RouterParam.VISITOR, visitor)
            .withParcelable(ARouterConstance.RouterParam.GIFT_PARCELABLE, data)
            .navigation()
    }

    /**
     * 常用的跳转 用户行为 到进入页面在处理
     * @param path 路由
     * I 访客携带的参数信息
     * I 访客
     */
    fun routerParcelable(
        path: String,
        visitor: VisitorBean?,
        data: Parcelable,
        compat: ActivityOptionsCompat? = null,
        activity: Activity,
    ) {
        try {
            ARouter.getInstance().build(path)
                .withParcelable(ARouterConstance.RouterParam.VISITOR, visitor)
                .withParcelable(ARouterConstance.RouterParam.GIFT_PARCELABLE, data)
                .withOptionsCompat(compat)
                .navigation(activity)
        } catch (e: Exception) {
            ToastUtils.show("路由地址错误")
        }
    }

    /**
     * 常用的跳转 用户行为 到进入页面在处理
     * @param path 路由
     * I 访客携带的参数信息
     * I 访客
     */
    fun routerSerializable(path: String, visitor: VisitorBean?, data: Serializable) {
        ARouter.getInstance().build(path)
            .withParcelable(ARouterConstance.RouterParam.VISITOR, visitor)
            .withSerializable(ARouterConstance.RouterParam.GIFT_SERIALIZABLE, data)
            .navigation()
    }

    /**
     * 常用的跳转 用户行为 到进入页面在处理
     * @param path 路由
     * I 访客携带的参数信息
     * I 访客
     */
    fun <I : Parcelable> routerArrayList(path: String, visitor: VisitorBean, data: ArrayList<I>) {
        ARouter.getInstance().build(path)
            .withParcelable(ARouterConstance.RouterParam.VISITOR, visitor)
            .withParcelableArrayList(ARouterConstance.RouterParam.GIFT_PARCELABLE, data)
            .navigation()
    }

    /**
     * 常用的跳转 用户行为 到进入页面在处理
     * @param path 路由
     */
    fun router(path: String, visitor: VisitorBean = VisitorBean()) {
        ARouter.getInstance().build(path)
            .withParcelable(ARouterConstance.RouterParam.VISITOR, visitor)
            .navigation()
    }


    /* */
    /**
     * 通用的话就需要更多的 协调  像是访问者信息  行为 和 被访问者信息 行为 等包装起来
     *//*
    fun <I : Serializable> routerCommonList(path: String, visitor: Visitor<I>) {
        ARouter.getInstance().build(path)
            .withSerializable(ARouterConstance.RouterParam.VISITOR, visitor)
            .navigation()
    }

    */
    /**
     * 通用的话就需要更多的 协调  像是访问者信息  行为 和 被访问者信息 行为 等包装起来
     *//*
    fun <I : Serializable> routerCommonDetail(path: String, visitor: Visitor<I>) {
        ARouter.getInstance().build(path)
            .withSerializable(ARouterConstance.RouterParam.VISITOR, visitor)
            .navigation()
    }

    */
    /**
     * 通用的话就需要更多的 协调  像是访问者信息  行为 和 被访问者信息 行为 等包装起来
     *//*
    fun <I : Serializable> routerCommonEdit(path: String, visitor: Visitor<I>) {
        ARouter.getInstance().build(path)
            .withSerializable(ARouterConstance.RouterParam.VISITOR, visitor)
            .navigation()
    }*/
}