package com.gk.world.comprehensive.ui.fragment.main

import androidx.fragment.app.activityViewModels
import com.drake.engine.base.EngineNavFragment
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.FragmentMainHomeBinding
import com.gk.world.comprehensive.ui.vm.MainViewModel

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
        setEnum()
    }

    private fun setEnum() {
        mainVm.enums.observe(viewLifecycleOwner, { list ->
            /*if (list.isNotEmpty()) {
                binding.rv.divider {
                    setDivider(8, true)
                }.grid(5).setup {
                    addType<HomeMenuItemBean>(R.layout.home_grid_item)
                    onClick(R.id.item) {
                        val m = getModel<HomeMenuItemBean>()
                        m.path?.let { it1 ->
                            if (ARouterConstance.CoreComponent.SIGN_IN_ACTIVITY == it1) {
                                XXPermissions.with(this@HomeFragment)
                                    .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                                    .request(object : OnPermissionCallback {
                                        override fun onGranted(
                                            permissions: MutableList<String>?,
                                            all: Boolean,
                                        ) {
                                            ARouterUtils.router(it1)
                                        }

                                        override fun onDenied(
                                            permissions: MutableList<String>?,
                                            never: Boolean,
                                        ) {
                                            super.onDenied(permissions, never)
                                            if (never) {
                                                ToastUtils.showLong("被永久拒绝授权，请手动授予定位权限")
                                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                                XXPermissions.startPermissionActivity(
                                                    requireActivity(),
                                                    permissions)
                                            } else {
                                                ToastUtils.showLong("获取定位权限失败")
                                            }
                                        }
                                    })
                            } else {
                                ARouterUtils.router(it1)
                            }
                        }
                    }
                    onBind {
                        val model = getModel<HomeMenuItemBean>()
                        val image = findView<AppCompatImageView>(R.id.image)
                        if (model.drawable > 0) {
                            image.setImageDrawable(resources.getDrawable(model.drawable))
                        } else {
                            setImageUrl(image, source = model.icon)
                        }
                    }
                }.models = list
            }*/

        })
    }

    override fun initView() {
        /*try {
            val list = arrayListOf<String>(resources.getString(R.string.my_p))
            binding.vp.adapter = MainHomePageAdapter(activity!!)
            binding.tab.setViewPager2(binding.vp, list)
//            getNoticeData()
            scopeDialog {
                val userDetail = Get<UserDetailBean>(HttpPortConst.Java.GET_USER_DETAIL) {
                    param("id", AppCacheManager.getGlobalUserInfoBean()?.userId)
                }.await()
                *//*存用户信息*//*
                AppCacheManager.getAftMmkv()
                    .encode(UserDetailBean::class.java.simpleName, userDetail)
                if (userDetail != null) {
                    setImageUrl(binding.photo, userDetail.headerUrl)
                    binding.part.text = userDetail.departmentName
                    binding.name.text = userDetail.userName
                }
            }
            binding.arrow.setOnClickListener {
                ARouterUtils.router(ARouterConstance.CoreComponent.AR_ACTIVITY)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }*/
    }

    private fun getNoticeData() {
        /*scopeNetLife {
            Post<NoticeDataBean>(HttpPortConst.Java.POST_NOTICE_PAGE_LIST + "?rows=1&page=" + 1) {
            }.await().let { iit ->
                if (iit.records.isNotEmpty()) {
                    binding.soundLayout.text = iit.records[0].title
                    binding.soundLayout.setOnClickListener {
                        ARouterUtils.routerParcelable(ARouterConstance.Notice.NOTICE_DETAIL_ACTIVITY,
                            VisitorBean(),
                            iit.records[0]
                        )
                    }
                }
            }
        }*/
    }
}