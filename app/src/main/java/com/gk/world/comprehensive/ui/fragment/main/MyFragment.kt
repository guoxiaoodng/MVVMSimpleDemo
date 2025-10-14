package com.gk.world.comprehensive.ui.fragment.main

import android.view.View
import com.drake.engine.base.EngineNavFragment
import com.drake.net.utils.scopeDialog
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.FragmentMainMyBinding
import com.gk.world.comprehensive.glide.setImageUrl
import com.gk.world.net.AppCacheManager
import com.gk.world.resouce.arouter.ARouterUtils
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.dynamic.kv.KeyValueClickListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 10:49
 *@description：
 */
class MyFragment : EngineNavFragment<FragmentMainMyBinding>(R.layout.fragment_main_my),
    View.OnClickListener {
    override fun initData() {
        /*val userDetail = AppCacheManager.getUserDetailBean()
        if (userDetail != null) {
            setImageUrl(binding.phoneImg, userDetail.headerUrl)
            binding.name.setValueText(userDetail.userName)
            binding.phone.setValueText(userDetail.phone)
            binding.identity.setValueText(userDetail.identyfyCode)
            binding.area.setValueText(userDetail.areaNames.toString())
            binding.department.setValueText(userDetail.departmentName)
            binding.role.setValueText(userDetail.roleNameList.toString())
        }*/
    }

    override fun initView() {
        /*binding.btnLogin.setOnClickListener {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .popupAnimation(PopupAnimation.TranslateFromBottom)
                .asConfirm(
                    "提示",
                    "确定退出",
                ) {
                    scopeDialog {
                        requireActivity().finish()
                        ARouterUtils.router(ARouterConstance.Main.LOGIN_ACTIVITY)
                    }
                }.show()
        }
        binding.alertPw.addClickListener(object : KeyValueClickListener{
            override fun addAnyClick(isEdit: Boolean) {
                ARouterUtils.router(ARouterConstance.Main.PASSWORD_MANAGE_ACTIVITY)
            }
        })*/
    }
}