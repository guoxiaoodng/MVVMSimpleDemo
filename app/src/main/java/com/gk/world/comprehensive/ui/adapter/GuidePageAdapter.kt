package com.gk.world.comprehensive.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gk.world.comprehensive.ui.fragment.ready.GuideViewFragment

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 11:06
 *@description：
 */
const val GUIDE_ONE = 0
const val GUIDE_TWO = 1
const val GUIDE_THREE = 2


class GuidePageAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {


    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        GUIDE_ONE to {
            GuideViewFragment(GUIDE_ONE)
        },
        GUIDE_TWO to {
            GuideViewFragment(GUIDE_TWO)
        },
        GUIDE_THREE to {
            GuideViewFragment(GUIDE_THREE)
        }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}