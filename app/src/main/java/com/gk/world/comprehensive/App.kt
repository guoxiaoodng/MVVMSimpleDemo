package com.gk.world.comprehensive

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.util.Log
import androidx.multidex.MultiDex
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import coil.util.CoilUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
//import com.dds.skywebrtc.SkyEngineKit
//import com.dds.skywebrtc.rtccore.voip.VoipEvent
import com.drake.brv.utils.BRV
import com.drake.engine.utils.AppUtils
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setLog
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.statelayout.StateConfig
import com.gk.world.comprehensive.works.RenameWorkerFactory
import com.gk.world.map.service.LocSdkClient
import com.gk.world.net.AppCacheManager
import com.gk.world.net.constance.http.HttpParamConst
import com.gk.world.net.converter.GsonConverter
import com.gk.world.resouce.config.FakeCrashLibrary
import com.gk.world.resouce.dialog.LoadingDialog
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tecent.tecentx5.TecentX5WebUtil
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
//import map.baidu.ar.init.ArSdkManager
//import map.baidu.ar.init.MKGeneralListener
//import map.baidu.ar.utils.ArBDLocation
import okhttp3.OkHttpClient
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit


class App : Application(), ImageLoaderFactory, Configuration.Provider {
    private val TAG = this.javaClass.simpleName
    lateinit var mVibrator: Vibrator
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initNetwork()
        initARouter()
        initState()
        initBugly()
        initTimber()
        initMmkv()
        BRV.modelId = BR.m
        ActivityUtils.addActivityLifecycleCallbacks(Utils.ActivityLifecycleCallbacks())
        // 初始化 Toast 框架
        ToastUtils.init(this);
        initGlide()
//        initBaidu()
//        // 初始化信令
//        SkyEngineKit.init(VoipEvent())
        //监控网络状态
        /*NetworkUtils.registerNetworkStatusChangedListener(object :
            NetworkUtils.OnNetworkStatusChangedListener {
            override fun onDisconnected() {
            }

            override fun onConnected(networkType: NetworkUtils.NetworkType?) {
            }

        })*/
        //x5内核初始化
        TecentX5WebUtil.getUtil().initX5Web(this)
    }

    /*private fun initBaidu() {
        *//***
         * 初始化定位sdk，建议在Application中创建
         *//*
        // 若用百度定位sdk,需要在此初始化定位SDK
        // 若用百度定位sdk,需要在此初始化定位SDK
        // ArSDK模块初始化

        // ArSDK模块初始化
        ArSdkManager.getInstance()
            .initApplication(this, MyGeneralListener(applicationContext))
        // 若用百度定位sdk,需要在此初始化定位SDK
        LocSdkClient.getInstance(this).locationStart
        mVibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        SDKInitializer.initialize(applicationContext)
        SDKInitializer.setCoordType(CoordType.BD09LL)
    }


    class MyGeneralListener(val app: Context) : MKGeneralListener {
        // 事件监听，用来处理通常的网络错误，授权验证错误等
        override fun onGetPermissionState(iError: Int) {
            // 非零值表示key验证未通过
            if (iError != 0) {
                // 授权Key错误：
                Timber.i("arsdk 验证异常，请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: $iError")
            } else {
                Timber.i("key认证成功")
            }
        }

        // 回调给ArSDK获取坐标（demo调用百度定位sdk）
        override fun onGetBDLocation(): ArBDLocation? {
            val location =
                LocSdkClient.getInstance(app).locationStart
                    .lastKnownLocation ?: return null
            val arBDLocation = ArBDLocation()
            // 设置经纬度信息
            arBDLocation.longitude = location.longitude
            arBDLocation.latitude = location.latitude
            return arBDLocation
        }
    }*/

    /**初始化glide*/
    private fun initGlide() {
        com.gk.world.comprehensive.glide.ImageLoader.getDefault().diskCacheOptions()
            .setDiskCacheDirPath(getExternalFilesDir("Cache")?.path ?: filesDir.path)
            .setDiskCacheFolderName("Image")
            .setDiskCacheSize(2 * 1024 * 1024) // 设置磁盘缓存2G
            .setBitmapPoolSize(2.0f)
            .setMemoryCacheSize(1.5f)
            .build()
    }

    /**初始化键值对缓存*/
    private fun initMmkv() {
        val rootDir = MMKV.initialize(this)
        Timber.i(TAG, "MMKV rootDir = $rootDir")
    }

    /**初始化打印*/
    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    /**初始化bug监控*/
    private fun initBugly() {
        CrashReport.initCrashReport(this);
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //65536
        MultiDex.install(this)
    }

    /**
     * 缺省页
     */
    private fun initState() {
        StateConfig.apply {
            emptyLayout = R.layout.layout_empty // 配置空布局
            errorLayout = R.layout.layout_error // 配置错误布局
            loadingLayout = R.layout.layout_loading // 配置加载中布局
        }
        /*初始化SmartRefreshLayout, 这是自动下拉刷新和上拉加载采用的第三方库
        [https://github.com/scwang90/SmartRefreshLayout/tree/master] V2版本*/
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }

    /**
     *
     * 全局配置网络请求
     */
    private fun initNetwork() {
        NetConfig.init(BuildConfig.BASE_URL) {
            // 超时设置
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            setLog(BuildConfig.DEBUG) // LogCat异常日志
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG)) // 添加日志记录器
            setRequestInterceptor(object : RequestInterceptor { // 添加请求拦截器
                override fun interceptor(request: BaseRequest) {
                    if (AppCacheManager.getGlobalUserInfoBean() != null) {
                        val b = AppCacheManager.getGlobalUserInfoBean()
                        if (b != null) {
                            //添加公共参数
                            request.addHeader("Authorization", "Bearer " + b.token)
                            request.addHeader(
                                HttpParamConst.ParamKey.VERSION,
                                AppUtils.getAppVersionName()
                            )
                        }
                    }
                }
            })
            setConverter(GsonConverter()) // 数据转换器
            setDialogFactory { // 全局加载对话框
                LoadingDialog(it).apply {

                }
            }
        }
    }

    /**
     * 初始化 路由框架
     */
    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
    }


    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        protected override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            FakeCrashLibrary.log(priority, tag, message)
            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t)
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t)
                }
            }
        }
    }

    /**
     * 初始化 coil 的ImageLoader*/
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .componentRegistry {
                add(VideoFrameFileFetcher(this@App))
                add(VideoFrameUriFetcher(this@App))
                add(VideoFrameDecoder(this@App))
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .build()
    }

    /**
     * WorkManager 配置项
     * */
    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(RenameWorkerFactory())
            .setMinimumLoggingLevel(Log.VERBOSE)
            .build()
}