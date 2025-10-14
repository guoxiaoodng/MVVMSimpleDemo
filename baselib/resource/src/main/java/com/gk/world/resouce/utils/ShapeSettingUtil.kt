package com.gk.world.resouce.utils

import android.R
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/5/23
 *@time: 9:31
 *@description：
 */
class ShapeSettingUtil {
    companion object {
        /**
         * 设置背景选择器
         *
         * @param pressedDraw 按下时所定义的Drawable
         * @param normalDraw  正常显示的Drawable
         * @return
         */
        fun getSelector(normalDraw: Drawable?, pressedDraw: Drawable?): StateListDrawable? {
            val stateListDrawable = StateListDrawable()
            stateListDrawable.addState(intArrayOf(R.attr.state_pressed), pressedDraw)
            stateListDrawable.addState(intArrayOf(), normalDraw)
            return stateListDrawable
        }


        /**
         * 设置shape
         *
         * @param radius 半径长度
         * @param fillColor 填充颜色
         * @param storeWidth 线条宽度
         * @param strokeColor 线条颜色
         * @return
         */
        fun getDrawable(
            radius: Int,
            fillColor: String?,
            storeWidth: Int,
            strokeColor: String?,
        ): GradientDrawable? {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.cornerRadius = radius.toFloat()
            gradientDrawable.setColor(Color.parseColor(fillColor))
            gradientDrawable.setStroke(storeWidth, Color.parseColor(strokeColor))
            return gradientDrawable
        }
    }
}