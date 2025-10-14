package com.gk.world.comprehensive.ui.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.ActivityReadyBinding
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.base.BaseActivity

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