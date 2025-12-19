package com.data.mobile.resouce.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.drake.engine.base.EngineActivity
import com.drake.engine.base.EngineFragment


/**
 * 基类 base activity
 * @param contentLayoutId 布局
 */
abstract class BaseLazyFragment<B : ViewDataBinding>(
    @LayoutRes contentLayoutId: Int = 0,
) : Fragment(contentLayoutId), View.OnClickListener {

    private var isViewCreated = false
    private var isVisibleToUser = false
    private var isDataLoaded = false


    lateinit var binding: B

    protected abstract fun initView()

    /**
     * 第一次可见时触发调用,此处实现具体的数据请求逻辑
     */
    protected abstract fun lazyInitData()

    override fun onClick(v: View) {}


    override fun onResume() {
        super.onResume()
        isViewCreated = true
        tryLoadData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryLoadData()
    }

    open fun tryLoadData() {
        if (isViewCreated && isVisibleToUser && isParentVisible() && !isDataLoaded) {
            lazyInitData()
            isDataLoaded = true
            //通知子Fragment请求数据
            dispatchParentVisibleState()
        }
    }


    /**
     * ViewPager场景下，当前fragment可见时，如果其子fragment也可见，则让子fragment请求数据
     */
    private fun dispatchParentVisibleState() {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments.isEmpty()) {
            return
        }
        for (child in fragments) {
            if (child is BaseLazyFragment<*> && child.isVisibleToUser) {
                child.tryLoadData()
            }
        }
    }

    /**
     * ViewPager场景下，判断父fragment是否可见
     */
    private fun isParentVisible(): Boolean {
        val fragment = parentFragment
        return (fragment == null || fragment is EngineFragment<*>
                || fragment is BaseLazyFragment<*> && fragment.isVisibleToUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        val engineActivity = (requireActivity() as? EngineActivity<*>)
        engineActivity?.onBackPressed(this::onBackPressed)
        try {
            initView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    open fun onBackPressed(): Boolean {
        return false
    }
}
