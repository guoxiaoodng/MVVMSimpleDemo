package com.gk.world.comprehensive.ui.fragment.main

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import com.blankj.utilcode.util.ToastUtils
import com.drake.engine.base.EngineNavFragment
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.FragmentMainHomeBinding
import com.gk.world.comprehensive.ui.popup.QRCodePopup
import com.gk.world.comprehensive.ui.vm.MainViewModel
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 10:49
 *@description：
 */
class HomeFragment : EngineNavFragment<FragmentMainHomeBinding>(R.layout.fragment_main_home) {

    private val mainVm: MainViewModel by activityViewModels()

    override fun initData() {
        activity?.intent?.let { handleActivityIntent(it) }
    }

    override fun initView() {
//        generateQRCodePopup()
        binding.tv.setOnClickListener {
            ToastUtils.showShort("点击了tv")
        }
        binding.et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                ToastUtils.showShort(p0.toString())
            }

        })
        binding.btnUp.setOnClickListener {
            ToastUtils.showShort("上")
        }
        binding.btnLeft.setOnClickListener {
            ToastUtils.showShort("左")
        }
        binding.btnRight.setOnClickListener {
            ToastUtils.showShort("右")
        }
    }

    private fun generateQRCodePopup() {
        // 生成一个唯一的ID用于标识这次输入
        val uniqueId = System.currentTimeMillis()

        // 方式1：使用服务器URL（推荐，更稳定）
        val serverUrl = "https://your-server.com/input-form?id=$uniqueId"

        // 方式2：使用自定义Scheme直接跳转
        val directUrl = "qrcodeinput://inputform?id=$uniqueId&time=${System.currentTimeMillis()}"

        // 显示二维码弹窗
        QRCodePopup.show(
            context = activity!!,
            qrData = directUrl, // 这里使用服务器URL
        )
    }

    private fun handleActivityIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.dataString
            data?.let {
                processDeepLinkData(it)
            }
        }

        // 处理分享的文本
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        sharedText?.let {
            if (it.isNotBlank()) {
                displayResult(it)
            }
        }
    }

    private fun processDeepLinkData(data: String) {
        if (data.startsWith("qrcodeinput://input/")) {
            val input = URLDecoder.decode(
                data.removePrefix("qrcodeinput://input/"),
                "UTF-8"
            )
            displayResult(input)
        }
    }

    private fun displayResult(input: String) {
        // 添加时间戳
        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val formattedText = "[$time] $input"
    }
}