package com.data.hemo.mobile.ui.activity.map

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.ActivityLocalMapBinding
import com.data.mobile.resouce.arouter.constance.ARouterConstance
import com.data.mobile.resouce.arouter.guest.VisitorBean
import com.data.mobile.resouce.base.BaseActivity

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