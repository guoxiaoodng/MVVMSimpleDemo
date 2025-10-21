package com.gk.world.comprehensive.ui.activity

import android.app.Activity
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.NetworkUtils
import com.gk.world.afloat.ToastCall
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.ActivityMainBinding
import com.gk.world.comprehensive.ui.adapter.MainPageAdapter
import com.gk.world.comprehensive.ui.vm.MainViewModel
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.base.BaseActivity
import timber.log.Timber

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 10:49
 *@description：主页面
 */
@Route(path = ARouterConstance.Main.MAIN_ACTIVITY)
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm: MainViewModel by viewModels()

    private var listener: MyNetworkStatusChangedListener? = null

    override fun initData() {
        vm.getEnums()
        requestSerialPort()
//        getVersion()
    }

    private fun requestSerialPort() {
        vm.requestSerialPortPermission(this)
    }


    override fun initView() {
        try {
            binding.vp.isUserInputEnabled = false
            binding.vp.adapter = MainPageAdapter(this)
            binding.navView.itemIconTintList = null
            // 当ViewPager切换页面时，改变ViewPager的显示
            binding.navView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        binding.vp.setCurrentItem(0, true)
                        true
                    }
                    R.id.analyse -> {
                        binding.vp.setCurrentItem(2, true)
                        true
                    }
                    R.id.app -> {
                        binding.vp.setCurrentItem(1, true)
                        true
                    }
                    R.id.my -> {
                        binding.vp.setCurrentItem(2, true)
                        true
                    }
                    else -> {
                        true
                    }
                }
            }

            listener = MyNetworkStatusChangedListener(this)
            NetworkUtils.registerNetworkStatusChangedListener(listener)
            uploadLocation()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    /**上报轨迹信息*/
    private fun uploadLocation() {
    }

    override fun onDestroy() {
        if (listener != null) {
            NetworkUtils.unregisterNetworkStatusChangedListener(listener)
        }
        super.onDestroy()
    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    /**获取版本信息*/
    /*private fun getVersion() {
        XXPermissions.with(this)
            .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    scopeNetLife {
                        Get<AppVersionBean>(HttpPortConst.Java.GET_APP_VERSION) {
                        }.await().let {
                            if (it.versionNumber.toInt() > AppUtils.getAppVersionCode(AppUtils.getAppPackageName())) {
                                XPopup.Builder(requireActivity())
                                    .dismissOnBackPressed(false)
                                    .dismissOnTouchOutside(false)
                                    .hasNavigationBar(false)
                                    .isDestroyOnDismiss(true)
                                    .popupAnimation(PopupAnimation.TranslateFromBottom)
                                    .asCustom(VersionPopup(this@MainActivity, it))
                                    .show()
                            }
                        }
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        ToastUtils.showLong("被永久拒绝授权，请手动授予相关权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(requireActivity(), permissions)
                    } else {
                        ToastUtils.showLong("获取相关权限失败")
                    }
                }

            })
    }
*/
    class MyNetworkStatusChangedListener(var activity: Activity) :
        NetworkUtils.OnNetworkStatusChangedListener {
        override fun onDisconnected() {
            ToastCall.showWarning(activity, "网络已断开")
        }

        override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        }

    }

}