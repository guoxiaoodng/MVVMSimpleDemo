package com.data.mobile.afloat

import android.app.Activity
import com.hjq.xtoast.XToast


object ToastCall {
    fun showWarning(activity: Activity, message: String) {
        show(activity, message, R.mipmap.ic_dialog_tip_warning)
    }

    fun showError(activity: Activity, message: String) {
        show(activity, message, R.mipmap.ic_dialog_tip_error)
    }

    fun showFinish(activity: Activity, message: String) {
        show(activity, message, R.mipmap.ic_dialog_tip_finish)
    }

    private fun show(activity: Activity, message: String, drawableId: Int) {
        XToast<XToast<*>>(activity).apply {
            setContentView(R.layout.toast_hint)
            // 设置成可拖拽的
            //setDraggable()
            // 设置显示时长
            setDuration(1500)
            // 设置动画样式
            setAnimStyle(android.R.style.Animation_Translucent)
            // 设置外层是否能被触摸
            //setOutsideTouchable(false)
            // 设置窗口背景阴影强度
            //setBackgroundDimAmount(0.5f)
            setImageDrawable(android.R.id.icon, drawableId)
            setText(android.R.id.message, message)
            /*setOnClickListener(
                android.R.id.message,
                XToast.OnClickListener<TextView?> { toast: XToast<*>, view: TextView? ->
                    // 点击这个 View 后消失
                    toast.cancel()
                    // 跳转到某个Activity
                    // toast.startActivity(intent);
                })*/
        }.show()
    }
}