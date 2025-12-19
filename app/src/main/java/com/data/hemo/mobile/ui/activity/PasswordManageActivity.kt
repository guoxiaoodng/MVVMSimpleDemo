package com.data.hemo.mobile.ui.activity

import android.view.animation.AnimationUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.net.utils.scopeDialog
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.ActivityPasswordManageBinding
import com.data.mobile.resouce.arouter.constance.ARouterConstance
import com.data.mobile.resouce.base.BaseActivity
import com.hjq.toast.ToastUtils

@Route(path = ARouterConstance.Main.PASSWORD_MANAGE_ACTIVITY)
class PasswordManageActivity :
    BaseActivity<ActivityPasswordManageBinding>(R.layout.activity_password_manage) {
    override fun initData() {
    }

    override fun initView() {
        binding.title.setRightText("提交") {
            val oldP = binding.oldEt.text.toString()
            val newP = binding.newEt.text.toString()
            val newP1 = binding.newAgainEt.text.toString()
            if (oldP.isEmpty()) {
                ToastUtils.show(binding.oldEt.hint.toString())
                binding.oldEt.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake_anim
                    )
                )
                return@setRightText
            }
            if (newP.isEmpty()) {
                ToastUtils.show(binding.newEt.hint.toString())
                binding.newEt.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake_anim
                    )
                )
                return@setRightText
            }
            if (newP1.isEmpty()) {
                ToastUtils.show(binding.newAgainEt.hint.toString())
                binding.newAgainEt.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake_anim
                    )
                )
                return@setRightText
            }
            if (newP1 != newP) {
                ToastUtils.show("两次新密码不一致")
                return@setRightText
            }
            scopeDialog {
//                val data = Get<String>(HttpPortConst.Java.ALERT_PAS) {
//                    param("newPassword", newP1)
//                    param("oldPassword", oldP)
//                }.await()
//                ToastUtils.show("密码修改成功,请重新登录")
//                ARouterUtils.router(ARouterConstance.Main.LOGIN_ACTIVITY, VisitorBean(visitorRouter = ARouterConstance.Main.PASSWORD_MANAGE_ACTIVITY))
            }
        }
    }
}