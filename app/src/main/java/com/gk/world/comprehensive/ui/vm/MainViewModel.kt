package com.gk.world.comprehensive.ui.vm

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.blankj.utilcode.util.ToastUtils
import com.gk.world.comprehensive.bean.HomeMenuItemBean
import com.gk.world.comprehensive.ui.activity.MainActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/2/9
 *@time: 16:33
 *@description：
 */
class MainViewModel : ViewModel() {
    val enums = MutableLiveData<ArrayList<HomeMenuItemBean>>()

    fun requestSerialPortPermission(activity: MainActivity) {
        XXPermissions.with(activity)
            .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        ToastUtils.showLong("获取相关权限成功")
                    } else {
                        ToastUtils.showLong("获取相关权限成功，部分权限未正常授予")
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        ToastUtils.showLong("被永久拒绝授权，请手动授予相关权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(activity, permissions)
                    } else {
                        ToastUtils.showLong("获取相关权限失败")
                    }
                }

            })
    }

    fun getEnums() {
        scopeNetLife {
            /*全部菜单*/
            /*Get<HomeMenuBean?>(HttpPortConst.Java.ALL_MENU) {
                param("page", 1)
                param("rows", 100)
            }.await().let {

            }*/
            /*val list = arrayListOf<HomeMenuItemBean>()
            list.add(HomeMenuItemBean(title = "靶向搜索", drawable = R.drawable.b_sc, path = ARouterConstance.CoreIntentEnterprise.POINT_SEARCH_LIST_ACTIVITY))
            list.add(HomeMenuItemBean(title = "通知消息",
                    drawable = R.drawable.notyf,
                    path = ARouterConstance.Notice.NOTICE_LIST_ACTIVITY))
            list.add(HomeMenuItemBean(title = "签到打卡",
                    drawable = R.drawable.sign,
                    path = ARouterConstance.CoreComponent.SIGN_IN_ACTIVITY))
            list.add(HomeMenuItemBean(title = "工作轨迹",
                    drawable = R.drawable.locus,
                    path = ARouterConstance.CoreComponent.WORK_LOCUS_ACTIVITY))
            list.add(HomeMenuItemBean(title = "招商政策", drawable = R.drawable.b_sc,
                    path = ARouterConstance.CoreComponent.INVEST_LIST_ACTIVITY))
            list.add(HomeMenuItemBean(title = "意向企业", drawable = R.drawable.b_sc, path = ARouterConstance.CoreIntentEnterprise.INTENT_ENTERPRISE_LIST_ACTIVITY))
            list.add(HomeMenuItemBean(title = "招商活动", drawable = R.drawable.b_sc, path = ARouterConstance.CoreComponent.INVEST_ACT_LIST_ACTIVITY))
            enums.value = list*/
        }
    }
}