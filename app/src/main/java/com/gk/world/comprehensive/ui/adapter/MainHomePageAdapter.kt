package com.gk.world.comprehensive.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gk.world.comprehensive.ui.fragment.main.HomeProjectFragment

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 11:06
 *@description：
 */
const val GTASKS = 0
const val REPAIR_ORDER = 1


class MainHomePageAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {


    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        GTASKS to {
            HomeProjectFragment()
        }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}