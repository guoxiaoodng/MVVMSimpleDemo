package com.data.hemo.mobile.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.ActivityReadyBinding
import com.data.mobile.resouce.arouter.constance.ARouterConstance
import com.data.mobile.resouce.base.BaseActivity

/**
 * 准备页面 - 包含所有的 主页面之前的页面
 */
@Route(path = ARouterConstance.Main.READY_ACTIVITY)
class ReadyActivity : BaseActivity<ActivityReadyBinding>(R.layout.activity_ready) {
    override fun initData() {}

    override fun initView() {
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}