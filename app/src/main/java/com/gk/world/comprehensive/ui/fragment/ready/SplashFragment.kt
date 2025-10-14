package com.gk.world.comprehensive.ui.fragment.ready

import android.os.CountDownTimer
import androidx.navigation.fragment.findNavController
import com.drake.engine.base.EngineNavFragment
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.FragmentSplashBinding
import com.gk.world.net.AppCacheManager

/**
 * 闪频页
 */
class SplashFragment : EngineNavFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    var start: CountDownTimer? = null
    override fun initData() {
    }

    override fun initView() {
        start = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.downTime.text = String.format(
                    getString(R.string.welcome_jump),
                    ((millisUntilFinished / 1000).toInt()).toString()
                )
            }

            override fun onFinish() {
                jump()
            }
        }.start()
        binding.downTime.setOnClickListener {
            jump()
        }
    }

    private fun jump() {
        start?.cancel()
        //引导页 guide 相关页面的逻辑
        val preMmkv = AppCacheManager.getPreMmkv()
        /*if (!preMmkv.decodeBool("firstOpen")) {
//            preMmkv.encode("firstOpen", true)
            findNavController().navigate(R.id.action_splash_to_guide)
        } else {
            if (AppCacheManager.getGlobalUserInfoBean() != null) {
                findNavController().navigate(R.id.action_splash_to_main)
            } else {
                findNavController().navigate(R.id.action_splash_to_login)
            }
            requireActivity().finish()
        }*/
    }

}