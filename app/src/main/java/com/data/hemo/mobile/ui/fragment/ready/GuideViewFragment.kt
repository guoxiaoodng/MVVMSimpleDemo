package com.data.hemo.mobile.ui.fragment.ready

import com.drake.engine.base.EngineNavFragment
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.FragmentGuideViewBinding
import com.data.hemo.mobile.ui.adapter.GUIDE_ONE
import com.data.hemo.mobile.ui.adapter.GUIDE_THREE
import com.data.hemo.mobile.ui.adapter.GUIDE_TWO

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/11
 *@time: 11:30
 *@description：
 */


class GuideViewFragment(var type: Int) :
    EngineNavFragment<FragmentGuideViewBinding>(R.layout.fragment_guide_view) {
    override fun initData() {
    }

    override fun initView() {
        when (type) {
            GUIDE_ONE -> {
                binding.icon.setImageDrawable(resources.getDrawable(R.drawable.guide1))
                binding.hint.text = "及时预警 降本增效"
            }
            GUIDE_TWO -> {
                binding.icon.setImageDrawable(resources.getDrawable(R.drawable.guide2))
                binding.hint.text = "全程在线 无缝对接"

            }
            GUIDE_THREE -> {
                binding.icon.setImageDrawable(resources.getDrawable(R.drawable.guide3))
                binding.hint.text = "精确管理 统一运筹"

            }
        }
    }
}