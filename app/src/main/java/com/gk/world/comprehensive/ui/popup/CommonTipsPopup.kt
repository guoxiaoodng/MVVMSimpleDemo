package com.gk.world.comprehensive.ui.popup

import android.content.Context
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import com.gk.world.comprehensive.R
import com.gk.world.resouce.arouter.ARouterUtils
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.lxj.xpopup.core.CenterPopupView

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/2/17
 *@time: 16:16
 *@description：
 */
class CommonTipsPopup(@NonNull context: Context) : CenterPopupView(context) {

    private var mContent : String? = null

    constructor(context: Context, content : String) : this(context) {
        this.mContent = content
    }

    override fun getImplLayoutId(): Int {
        return R.layout.popup_common_tips
    }

    override fun onCreate() {
        super.onCreate()
        val content = findViewById<AppCompatTextView>(R.id.content)
        findViewById<AppCompatTextView>(R.id.title).setOnClickListener {
            dismiss()
        }
        findViewById<AppCompatTextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        content.text = mContent
        findViewById<AppCompatTextView>(R.id.tv_sure).setOnClickListener {
            dismiss()
            ARouterUtils.router(ARouterConstance.Main.LOGIN_ACTIVITY)
        }
    }
}