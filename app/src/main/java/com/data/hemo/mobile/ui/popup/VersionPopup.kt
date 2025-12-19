package com.data.hemo.mobile.ui.popup

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.NotificationUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.net.Get
import com.drake.net.component.Progress
import com.drake.net.interfaces.ProgressListener
import com.drake.net.utils.scopeNetLife
import com.data.hemo.mobile.R
import com.data.hemo.mobile.bean.AppVersionBean
import com.data.hemo.mobile.works.Constants
import com.data.mobile.net.converter.FastJsonConverter
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.lxj.xpopup.core.CenterPopupView
import timber.log.Timber
import java.io.File

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/2/17
 *@time: 16:16
 *@description：
 */
class VersionPopup(@NonNull context: Context) : CenterPopupView(context) {

    private var mBean: AppVersionBean? = null

    private var mContext: Context? = null

    private var signControl = false
    private var file: File? = null

    constructor(context: Context, bean: AppVersionBean) : this(context) {
        this.mBean = bean
        this.mContext = context
    }

    override fun getImplLayoutId(): Int {
        return R.layout.version_dialog
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        val tvContent = findViewById<AppCompatTextView>(R.id.tv_content)
        val btnUpdate = findViewById<AppCompatButton>(R.id.goto_update)
        val btnNext = findViewById<AppCompatButton>(R.id.next)
        val downPro = findViewById<ProgressBar>(R.id.download_progress)
        val tvPro = findViewById<AppCompatTextView>(R.id.download_progress_text)
        val clProgress = findViewById<ConstraintLayout>(R.id.cl_progress)
        file =
            File(mContext?.cacheDir?.absolutePath + AppUtils.getAppName() + mBean?.versionName + Constants.APK)
        if (file?.exists() == true) {
            file?.delete()
        }
        if (mBean?.forceUpdate == true) {
            btnNext.visibility = GONE
        } else {
            btnNext.visibility = VISIBLE
        }
        tvContent.text = "发现新版本V" + mBean?.versionNumber
        btnUpdate.setOnClickListener {
            try {
                if (file?.exists() == false) {
                    btnUpdate.isClickable = false
                    btnUpdate.background.alpha = 180
                    btnUpdate.text = "下载中"
                    tvContent.text =
                        "V" + mBean?.versionNumber + "更新中, 请稍后..."
                    if (StringUtils.isEmpty(mBean?.packagePath) || !mBean?.packagePath?.endsWith(
                            Constants.APK)!!
                    ) {
                        ToastUtils.showShort("安装包格式错误，无法更新")
                        dismiss()
                        return@setOnClickListener
                    }
                    clProgress.visibility = VISIBLE
                    scopeNetLife {
                        mBean?.packagePath?.let { it1 ->
                            file = Get<File>(it1) {
                                converter = FastJsonConverter()
                                setDownloadFileName(AppUtils.getAppName() + mBean?.versionName + Constants.APK)
                                setDownloadDir(mContext?.cacheDir)
                                setDownloadMd5Verify()
                                setDownloadTempFile()
                                addDownloadListener(object : ProgressListener() {
                                    override fun onProgress(p: Progress) {
                                        downPro.post {
                                            val progress = p.progress()
                                            downPro.progress = progress
                                            tvPro.text = "$progress%"
                                            if (progress == 100) {
                                                btnUpdate.text = "安装"
                                                tvContent.text = "下载完成"
                                                NotificationUtils.cancel(Constants.VERSION_APP_ID)
                                                btnUpdate.isClickable = true
                                                btnUpdate.background.alpha = 255
                                            } else {
                                                NotificationUtils.notify(Constants.VERSION_APP_ID) { t ->
                                                    t?.setContentTitle("正在下载" + AppUtils.getAppName() + mBean?.versionName)
                                                        ?.setContentText("下载进度")
                                                        ?.setSmallIcon(R.mipmap.ic_launcher)
                                                        ?.setNotificationSilent()
                                                        ?.setPriority(NotificationCompat.PRIORITY_LOW)
                                                        ?.setProgress(100, progress, false)
                                                }
                                            }
                                        }
                                    }
                                })
                            }.await()
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        XXPermissions.with(mContext)
                            .permission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                            .request(object : OnPermissionCallback {
                                override fun onGranted(
                                    permissions: MutableList<String>?,
                                    all: Boolean,
                                ) {
                                    installApk()
                                }

                                override fun onDenied(
                                    permissions: MutableList<String>?,
                                    never: Boolean,
                                ) {
                                    super.onDenied(permissions, never)
                                }

                            })
                    } else {
                        installApk()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        btnNext.setOnClickListener {
            dismiss()
        }
    }

    private fun installApk() {
        if (file != null && file?.exists() == true) {
            AppUtils.installApp(file)
        }
    }
}