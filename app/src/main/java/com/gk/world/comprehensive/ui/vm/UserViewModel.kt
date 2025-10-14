package com.gk.world.comprehensive.ui.vm

import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drake.net.Post
import com.drake.net.utils.scopeDialog
import com.gk.world.afloat.ToastCall
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.FragmentLoginBinding
import com.gk.world.net.AppCacheManager
import com.gk.world.net.bean.GlobalUserInfoBean
import com.gk.world.net.bean.LoginBean
import com.gk.world.net.constance.http.HttpPortConst
import com.gk.world.resouce.arouter.ARouterUtils
import com.gk.world.resouce.arouter.constance.ARouterConstance
import java.util.concurrent.CancellationException

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/8
 *@time: 9:41
 *@description：
 */
class UserViewModel : ViewModel() {
    // 用户信息
    private val userInfo = MutableLiveData<GlobalUserInfoBean>()

    /**
     * 拉取用户信息, 会自动通知页面更新, 同时页面销毁会自动取消网络请求
     */
    fun fetchUserInfo(
        activity: FragmentActivity,
        binding: FragmentLoginBinding,
        isConsent: Boolean,
    ) {
        when {
            !isConsent -> {
                binding.agreementS.startAnimation(
                    AnimationUtils.loadAnimation(
                        activity, R.anim.shake_anim
                    )
                )
                binding.agreement.startAnimation(
                    AnimationUtils.loadAnimation(
                        activity, R.anim.shake_anim
                    )
                )
                ToastCall.showWarning(activity, binding.agreement.text.toString())
            }
            binding.authCodeImg.tag.toString().isNullOrEmpty() -> {
                ToastCall.showWarning(activity, "为获取到图文验证码，无法登录")
                return
            }

            binding.name.text.isNullOrEmpty() -> {
                binding.name.startAnimation(
                    AnimationUtils.loadAnimation(
                        activity, R.anim.shake_anim
                    )
                )
                return
            }
            binding.ps.text.isNullOrEmpty() -> {
                binding.ps.startAnimation(
                    AnimationUtils.loadAnimation(
                        activity, R.anim.shake_anim
                    )
                )
                return
            }

            binding.authCode.text.isNullOrEmpty() -> {
                binding.authCode.startAnimation(
                    AnimationUtils.loadAnimation(
                        activity, R.anim.shake_anim
                    )
                )
                return
            }
            else -> activity.scopeDialog {
                Post<GlobalUserInfoBean>(HttpPortConst.Java.UserCenter.LOGIN) {
                    json(
                        "loginName" to binding.name.text.toString(),
                        "code" to binding.authCode.text.toString(),
                        "uuid" to binding.authCodeImg.tag,
                        "password" to binding.ps.text.toString()/*.encrypt()*/
                    )
                }.await().let {
                    it.password = binding.ps.text.toString()
                    it.loginName = binding.name.text.toString()
                    //存 登录数据
                    AppCacheManager.getPreMmkv().encode(
                        LoginBean::class.java.simpleName,
                        LoginBean(
                            it.loginName,
                            it.password
                        )
                    )
                    /*存用户信息*/
                    AppCacheManager.getAftMmkv()
                        .encode(GlobalUserInfoBean::class.java.simpleName, it)
                    /*跳转*/
                    ARouterUtils.router(ARouterConstance.Main.MAIN_ACTIVITY)
                    userInfo.value = it
                    activity.finish()
                }
            }.finally {
                // 关闭对话框后执行的异常
                if (it is CancellationException) {
                    ToastCall.showWarning(activity, "您取消了当前操作")
                }
            }
        }
    }
}