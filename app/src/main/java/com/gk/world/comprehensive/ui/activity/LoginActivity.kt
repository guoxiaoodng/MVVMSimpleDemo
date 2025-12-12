package com.gk.world.comprehensive.ui.activity

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.drake.net.Get
import com.drake.net.utils.scopeDialog
import com.drake.spannable.movement.ClickableMovementMethod
import com.drake.spannable.replaceSpan
import com.drake.spannable.span.HighlightSpan
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.bean.AuthCodeBean
import com.gk.world.comprehensive.databinding.FragmentLoginBinding
import com.gk.world.comprehensive.ui.vm.UserViewModel
import com.gk.world.net.AppCacheManager
import com.gk.world.net.bean.LoginBean
import com.gk.world.net.constance.http.HttpPortConst
import com.gk.world.net.converter.FastJsonConverter
import com.gk.world.resouce.arouter.ARouterUtils
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.arouter.guest.VisitorBean
import com.gk.world.resouce.base.BaseActivity


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/11
 *@time: 11:14
 *@description：
 */
@Route(path = ARouterConstance.Main.LOGIN_ACTIVITY)
class LoginActivity : BaseActivity<FragmentLoginBinding>(R.layout.fragment_login) {
    private val userViewModel: UserViewModel by viewModels() // 创建ViewModel

    @Autowired(name = ARouterConstance.RouterParam.VISITOR)
    @JvmField
    var v: VisitorBean? = null

    /**同意协议*/
    private var isConsent = false
    override fun initData() {
    }

    override fun initView() {
        ARouter.getInstance().inject(this)
        AppCacheManager.getAftMmkv().clearAll()
        ActivityUtils.finishAllActivitiesExceptNewest()
        val loginBean = AppCacheManager.getPreMmkv()
            .decodeParcelable(LoginBean::class.java.simpleName, LoginBean::class.java)

        loginBean?.let {
            if (v != null && ARouterConstance.Main.PASSWORD_MANAGE_ACTIVITY == v?.visitorRouter) {
                loginBean.loginPas = ""
                AppCacheManager.getPreMmkv().encode(
                    LoginBean::class.java.simpleName,
                    loginBean
                )
            }
            if (!it.loginName.isNullOrEmpty()) {
                binding.name.setText(it.loginName)
            }
            if (!it.loginPas.isNullOrEmpty()) {
                binding.ps.setText(it.loginPas)
            }
        }

        /*
         * 用户协议文本
         */
        binding.agreement.movementMethod = ClickableMovementMethod.getInstance()
        binding.agreement.text = getString(R.string.agreement)
            .replaceSpan("《隐私政策》") {
//                HighlightSpan(Color.parseColor("#ed6a2c")) {
//                    ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
//                        null,
//                        MediaBean("file:///android_asset/yszc.html", "隐私政策")
//                    )
//                }
            }.replaceSpan("《用户协议》") {
                HighlightSpan(
                    Color.parseColor("#4a70d2"),
                    Typeface.defaultFromStyle(Typeface.BOLD)
                ) {
//                    ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
//                        null,
//                        MediaBean("file:///android_asset/user_xy.html", "用户协议")
//                    )
                }
            }
        initClick()
        getAuthCode()
    }

    /**获取验证码*/
    private fun getAuthCode() {
        scopeDialog {
            val data = Get<AuthCodeBean>(HttpPortConst.Java.GET_AUTH_CODE) {
                converter = FastJsonConverter()
            }.await()
            val gg = data.img.replace("data:image/png;base64,", "")
            val decodedString: ByteArray = Base64.decode(gg, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.authCodeImg.setImageBitmap(decodedByte)
            binding.authCodeImg.tag = data.uuid
        }
    }


    /**
     * 处理点击事件
     */
    private fun initClick() {
        try {
            binding.authCodeImg.setOnClickListener {
                getAuthCode()
            }
            binding.agreementS.setOnClickListener {
                isConsent = !isConsent
                if (isConsent) {
                    binding.agreementS.setImageResource(R.drawable.consent)
                    binding.btnLogin.background =
                        resources.getDrawable(R.drawable.shape_b_theme_c_2)
                } else {
                    binding.agreementS.setImageResource(R.drawable.unconsent)
                    binding.btnLogin.background =
                        resources.getDrawable(R.drawable.shape_b_theme40_c_4)
                }
            }
            binding.name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    try {
                        if (s.isNullOrEmpty()) {
                            binding.delete.visibility = View.INVISIBLE
                        } else {
                            binding.delete.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
            binding.delete.setOnClickListener {
                binding.name.setText("")
                binding.ps.setText("")
            }
            binding.vG.setOnClickListener {
                if (binding.ps.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    binding.vG.setImageResource(R.drawable.gone)
                    binding.ps.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                } else {
                    binding.vG.setImageResource(R.drawable.visible)
                    binding.ps.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }
                binding.ps.text?.length?.let { it1 -> binding.ps.setSelection(it1.minus(1)) }
            }
            binding.btnLogin.setOnClickListener {
                try {
                    KeyboardUtils.hideSoftInput(this)
                    userViewModel.fetchUserInfo(
                        requireActivity(),
                        binding,
                        isConsent
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}