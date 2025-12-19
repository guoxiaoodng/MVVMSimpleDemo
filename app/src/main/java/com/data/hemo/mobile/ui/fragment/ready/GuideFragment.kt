package com.data.hemo.mobile.ui.fragment.ready

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.drake.engine.base.EngineNavFragment
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.FragmentGuideBinding
import com.data.hemo.mobile.ui.adapter.GuidePageAdapter
import com.data.mobile.net.AppCacheManager

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/11
 *@time: 11:30
 *@description：
 */
class GuideFragment : EngineNavFragment<FragmentGuideBinding>(R.layout.fragment_guide) {
    override fun initData() {
    }

    override fun initView() {
        val guidePageAdapter = GuidePageAdapter(activity!!)
        binding.vp2.adapter = guidePageAdapter
        binding.vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == guidePageAdapter.itemCount - 1) {
                    binding.start.visibility = View.VISIBLE
                    binding.jump.visibility = View.GONE
                } else {
                    binding.start.visibility = View.GONE
                    binding.jump.visibility = View.VISIBLE
                }
            }
        })
        binding.ib.setViewPager2(binding.vp2)
        binding.start.setOnClickListener {
            jump()
        }
        binding.jump.setOnClickListener {
            jump()
        }
    }

    private fun jump() {
        findNavController().navigate(R.id.action_guide_to_login)
        AppCacheManager.getPreMmkv().encode("firstOpen", true)
        requireActivity().finish()
    }
}