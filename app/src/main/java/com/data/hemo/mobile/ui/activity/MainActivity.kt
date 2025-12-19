package com.data.hemo.mobile.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.NetworkUtils
import com.data.mobile.afloat.ToastCall
import com.data.hemo.mobile.R
import com.data.hemo.mobile.databinding.ActivityMainBinding
import com.data.hemo.mobile.server.LocalHttpServer
import com.data.hemo.mobile.ui.vm.MainViewModel
import com.data.hemo.mobile.utils.QRCodeUtils
import com.data.mobile.resouce.arouter.constance.ARouterConstance
import com.data.mobile.resouce.base.BaseActivity
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/1/13
 *@time: 10:49
 *@description：主页面
 */
@SuppressLint("SetTextI18n")
@Route(path = ARouterConstance.Main.MAIN_ACTIVITY)
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val vm: MainViewModel by viewModels()

    private lateinit var httpServer: LocalHttpServer
    private var serverJob: Job? = null
    private var currentToken: String? = null

    private var listener: MyNetworkStatusChangedListener? = null

    override fun initData() {
        vm.getEnums()
        requestSerialPort()
    }

    private fun requestSerialPort() {
        vm.requestSerialPortPermission(this)
        vm.permissionStatus.observe(this) {
            if (it) {
                updateNetworkStatus()
            }
        }
    }

    private fun setupViews() {
        binding.btnGenerate.setOnClickListener {
            if (!com.data.hemo.mobile.utils.NetworkUtils.isWifiConnected(this)) {
                showWifiWarning()
                return@setOnClickListener
            }
            startOrUpdateServer()
        }

        binding.btnStopServer.setOnClickListener {
            stopServer()
        }

        binding.btnOpenWifi.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        binding.btnShare.setOnClickListener {
            shareQRCode()
        }

        // 检查网络状态
        updateNetworkStatus()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleDeepLink(it) }
    }

    override fun initView() {
        try {
            setupViews()
            listener = MyNetworkStatusChangedListener(this)
            NetworkUtils.registerNetworkStatusChangedListener(listener)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun updateNetworkStatus() {
        binding.tvNetworkStatus.text = com.data.hemo.mobile.utils.NetworkUtils.getNetworkInfo(this)

        if (!com.data.hemo.mobile.utils.NetworkUtils.isWifiConnected(this)) {
            binding.btnGenerate.isEnabled = false
            binding.btnGenerate.text = "请先连接WIFI"
        } else {
            binding.btnGenerate.isEnabled = true
            binding.btnGenerate.text = "生成二维码"
        }
    }

    private fun startOrUpdateServer() {
        if (!this::httpServer.isInitialized) {
            httpServer = LocalHttpServer(this, 8080)

            httpServer.setDataReceiver { content ->
                runOnUiThread {
                    showReceivedData(content)
                }
            }

            try {
                httpServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false)
                binding.tvStatus.text = "✅ 服务器已启动"
                ToastCall.showFinish(this, "本地服务器启动成功")
            } catch (e: Exception) {
                ToastCall.showError(this, "启动服务器失败: ${e.message}")
                return
            }
        }

        // 生成新的token和二维码
        currentToken = httpServer.generateToken()
        val serverUrl = httpServer.getServerUrl(currentToken!!)

        // 生成二维码
        val qrCode = QRCodeUtils.generateQRCodeBitmap(serverUrl, 600)
        binding.ivQrcode.setImageBitmap(qrCode)

        // 显示服务器信息
        binding.tvServerInfo.text = """
            服务器地址: ${serverUrl.replace("?token=${currentToken}", "")}
            访问Token: $currentToken
            
            使用说明：
            1. 确保另一台手机连接同一WiFi
            2. 使用相机或扫码应用扫描二维码
            3. 在打开的页面中输入内容并提交
            4. 内容将自动显示在本机
        """.trimIndent()

        // 启动数据检查任务
        startDataCheckTask()
    }

    private fun startDataCheckTask() {
        serverJob?.cancel()
        serverJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(2000) // 每2秒检查一次
                currentToken?.let { token ->
                    val data = httpServer.checkData(token)
                    if (data != null) {
                        launch(Dispatchers.Main) {
                            showReceivedData(data)
                            httpServer.clearSession(token)
                        }
                    }
                }
            }
        }
    }

    private fun stopServer() {
        serverJob?.cancel()
        serverJob = null

        if (this::httpServer.isInitialized) {
            httpServer.stop()
            binding.tvStatus.text = "❌ 服务器已停止"
            binding.ivQrcode.setImageResource(android.R.color.transparent)
            binding.tvServerInfo.text = "服务器未运行"
        }
        ToastCall.showFinish(this, "服务器已停止")
    }

    private fun showReceivedData(content: String) {
        // 添加调试日志
        Timber.d("Received content: $content")
        Timber.d("Content length: ${content.length}")
        Timber.d("Content bytes: ${content.toByteArray().joinToString()}")
        AlertDialog.Builder(this)
            .setTitle("📨 收到新数据")
            .setMessage("内容：\n\n${content.take(500)}${if (content.length > 500) "..." else ""}")
            .setPositiveButton("复制") { _, _ ->
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("QR Data", content)
                clipboard.setPrimaryClip(clip)
                ToastCall.showFinish(this, "已复制到剪贴板")
            }
            .setNegativeButton("关闭", null)
            .setNeutralButton("查看详情") { _, _ ->
//                val intent = Intent(this, ResultActivity::class.java).apply {
//                    putExtra("content", content)
//                }
//                startActivity(intent)
            }
            .show()
    }

    private fun showWifiWarning() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ 网络提示")
            .setMessage("检测到未连接WiFi，请在设置中连接WiFi网络\n\n两台设备需要在同一网络下才能传输数据")
            .setPositiveButton("去连接") { _, _ ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun shareQRCode() {
        currentToken?.let { token ->
            val serverUrl = httpServer.getServerUrl(token)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "扫描此二维码传输数据：\n$serverUrl")
                putExtra(Intent.EXTRA_SUBJECT, "QR数据传输链接")
            }

            startActivity(Intent.createChooser(shareIntent, "分享二维码链接"))
        } ?: run {
            ToastCall.showWarning(this, "请先生成二维码")
        }
    }

    private fun handleDeepLink(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri?.scheme == "qrdata") {
                val content = uri.getQueryParameter("content")
                content?.let {
                    showReceivedData(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateNetworkStatus()
    }

    override fun onDestroy() {
        if (listener != null) {
            NetworkUtils.unregisterNetworkStatusChangedListener(listener)
        }
        super.onDestroy()
    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    /**获取版本信息*/
    /*private fun getVersion() {
        XXPermissions.with(this)
            .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    scopeNetLife {
                        Get<AppVersionBean>(HttpPortConst.Java.GET_APP_VERSION) {
                        }.await().let {
                            if (it.versionNumber.toInt() > AppUtils.getAppVersionCode(AppUtils.getAppPackageName())) {
                                XPopup.Builder(requireActivity())
                                    .dismissOnBackPressed(false)
                                    .dismissOnTouchOutside(false)
                                    .hasNavigationBar(false)
                                    .isDestroyOnDismiss(true)
                                    .popupAnimation(PopupAnimation.TranslateFromBottom)
                                    .asCustom(VersionPopup(this@MainActivity, it))
                                    .show()
                            }
                        }
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        ToastUtils.showLong("被永久拒绝授权，请手动授予相关权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(requireActivity(), permissions)
                    } else {
                        ToastUtils.showLong("获取相关权限失败")
                    }
                }

            })
    }
*/
    class MyNetworkStatusChangedListener(var activity: Activity) :
        NetworkUtils.OnNetworkStatusChangedListener {
        override fun onDisconnected() {
            ToastCall.showWarning(activity, "网络已断开")
        }

        override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        }

    }

}