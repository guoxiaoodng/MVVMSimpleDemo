package com.data.mobile.resouce.arouter.constance

import com.data.mobile.resouce.dynamic.single.CommonListItemNextParamsInterface

/**
 * 综合治理 组件 核心业务  路由
 *
 */
class ARouterConstance {
    object RouterParam {
        /** Router 访问者 基本信息 [com.data.mobile.resouce.arouter.guest.VisitorBean]*/
        const val VISITOR = "Visitor"

        /**礼品 访问者携带的东西  实现了 PARCELABLE 对象*/
        const val GIFT_PARCELABLE = "GIFT_PARCELABLE"

        /**礼品 访问者携带的东西  实现了 [CommonListItemNextParamsInterface] 的 对象 返回的map的to json*/
        const val GIFT_SERIALIZABLE = "GIFT_SERIALIZABLE"
    }

    object Main {
        private const val PRE = "/main/"

        /**
         * 主界面
         */
        const val MAIN_ACTIVITY = PRE + "MainActivity"

        /**
         * 主界面 前的页面
         */
        const val READY_ACTIVITY = PRE + "ReadyActivity"

        /**
         * 登录
         */
        const val LOGIN_ACTIVITY = PRE + "LoginActivity"

        /**
         * 修改密码
         */
        const val PASSWORD_MANAGE_ACTIVITY = PRE + "PasswordManageActivity"
    }

    object BaiduMap {
        private const val PRE = "/Map/"

        /**
         * 入园申请 处理记录
         */
        const val LOCAL_MAP_ACTIVITY = PRE + "LocalMapActivity"
    }

    object X5 {
        private const val PRE = "/X5/"

        /**
         * 入园申请 处理记录
         */
        const val X5_WEB_VIEW = PRE + "MediaPreviewActivity"
    }

    object Setting {
        private const val PRE = "/Setting/"

        /**
         * 通知公告列表
         */
        const val SETTING_ACTIVITY = PRE + "SettingActivity"
    }
}