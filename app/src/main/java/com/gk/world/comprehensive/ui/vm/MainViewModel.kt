package com.gk.world.comprehensive.ui.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.bean.HomeMenuItemBean
import com.gk.world.resouce.arouter.constance.ARouterConstance

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/2/9
 *@time: 16:33
 *@description：
 */
class MainViewModel : ViewModel() {
    val enums = MutableLiveData<ArrayList<HomeMenuItemBean>>()
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