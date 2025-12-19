package com.data.mobile.resouce.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.drake.engine.base.EngineActivity
import com.drake.statusbar.immersive
import com.drake.statusbar.setActionBarTransparent


/**
 * 基类 base activity
 * @param contentLayoutId 布局
 */
abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes contentLayoutId: Int = 0,
) : EngineActivity<B>(contentLayoutId) {
    /**
     * 当前页的标签
     */
    val tag: String = this.javaClass.simpleName + "  "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersive(darkMode = true)
        setActionBarTransparent()
    }

    /**
     * 当前页的标签
     * @param thiz
     */
    fun openRouterInject(thiz: Any) {
        ARouter.getInstance().inject(thiz)
    }
}
