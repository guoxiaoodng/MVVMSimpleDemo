package com.data.hemo.mobile.ui.activity.setting

import com.alibaba.android.arouter.facade.annotation.Route
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.ActivitySettingBinding
import com.data.mobile.resouce.arouter.constance.ARouterConstance
import com.data.mobile.resouce.base.BaseActivity

/**
 * 创建日期：2022/3/2 10:52
 * @author gxd
 * @version 1.0
 * 包名： com.data.hemo.mobile.ui.activity.setting
 * 类说明：
 */
@Route(path = ARouterConstance.Setting.SETTING_ACTIVITY)
class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    override fun initData() {
    }

    override fun initView() {
        /*binding.tvExit.setOnClickListener {
            XPopup.Builder(this).dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .hasNavigationBar(false)
                .isDestroyOnDismiss(true)
                .popupAnimation(PopupAnimation.TranslateFromBottom)
                .asCustom(CommonTipsPopup(this, "确认退出登录吗？")).show()
        }

        binding.tvVersionCode.text =
            "V" + AppUtils.getAppVersionCode(AppUtils.getAppPackageName()).toString()

        binding.clVersion.setOnClickListener {
            scopeNetLife {
                val data = Get<AppVersionBean>(HttpPortConst.Java.GET_APP_VERSION) {
                }.await()
                if (data.versionNumber.toInt() > AppUtils.getAppVersionCode(AppUtils.getAppPackageName())) {
                    XPopup.Builder(this@SettingActivity)
                        .dismissOnBackPressed(false)
                        .dismissOnTouchOutside(false)
                        .hasNavigationBar(false)
                        .isDestroyOnDismiss(true)
                        .popupAnimation(PopupAnimation.TranslateFromBottom)
                        .asCustom(VersionPopup(this@SettingActivity, data))
                        .show()

                }
            }.finally {
                ToastUtils.showShort("未发现新版本")
            }
        }*/
    }
}