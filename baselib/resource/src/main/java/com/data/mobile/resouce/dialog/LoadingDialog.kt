package com.data.mobile.resouce.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.data.mobile.resource.R

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/28
 *@time: 15:14
 *@description：
 */
class LoadingDialog(con: Context) : Dialog(con, R.style.CustomDialog) {
    private var lottieView: LottieAnimationView? = null

    init {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.loading_layout);
        val at = window?.attributes
        at?.width = WindowManager.LayoutParams.WRAP_CONTENT
        at?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = at
        lottieView = findViewById(R.id.lottie_view)
        lottieView?.playAnimation()
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        lottieView?.cancelAnimation()
        lottieView = null
        super.dismiss()
    }
}