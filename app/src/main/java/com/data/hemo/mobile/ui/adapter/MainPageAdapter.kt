package com.data.hemo.mobile.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.data.hemo.mobile.ui.fragment.main.AppFragment
import com.data.hemo.mobile.ui.fragment.main.HomeFragment
import com.data.hemo.mobile.ui.fragment.main.MyFragment

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 11:06
 *@description：
 */
const val HOME = 0
const val APP = 1
const val MY = 2


class MainPageAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {


    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HOME to { HomeFragment() },
        APP to { AppFragment() },
        MY to { MyFragment() },
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}