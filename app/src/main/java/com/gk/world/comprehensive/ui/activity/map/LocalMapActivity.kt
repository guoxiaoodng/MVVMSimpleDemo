package com.gk.world.comprehensive.ui.activity.map

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult
import com.drake.channel.sendEvent
import com.gk.world.comprehensive.R
import com.gk.world.comprehensive.databinding.ActivityLocalMapBinding
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.arouter.guest.MessageTypeEnum
import com.gk.world.resouce.arouter.guest.VisitorBean
import com.gk.world.resouce.arouter.guest.VisitorCallMessage
import com.gk.world.resouce.base.BaseActivity
import com.hjq.toast.ToastUtils

@Route(path = ARouterConstance.BaiduMap.LOCAL_MAP_ACTIVITY)
class LocalMapActivity : BaseActivity<ActivityLocalMapBinding>(R.layout.activity_local_map) {

    @Autowired(name = ARouterConstance.RouterParam.VISITOR)
    @JvmField
    var v: VisitorBean? = null

    @Autowired(name = ARouterConstance.RouterParam.GIFT_PARCELABLE)
    @JvmField
    var data: Any? = null


    private var result: ReverseGeoCodeResult? = null
    override fun initData() {
    }

    override fun initView() {
        /*try {
            ARouter.getInstance().inject(this)
            binding.title.setRightText(R.string.sure) {
                if (result?.address.isNullOrEmpty()) {
                    ToastUtils.show("为获取的地址信息")
                }
                val visitorCallBean =
                    VisitorCallMessage(MessageTypeEnum.Parcel).put(result!!)
                sendEvent(visitorCallBean, v?.visitorRouter!!)
                finish()
            }
            binding.map.setListening {
                result = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
    }

    override fun onDestroy() {
        binding.map.onDestroy()
        super.onDestroy()
    }
}