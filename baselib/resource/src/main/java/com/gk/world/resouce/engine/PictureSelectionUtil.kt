package com.gk.world.resouce.engine

import android.content.Context
import com.gk.world.resouce.config.GlideEngine
import com.gk.world.resouce.dynamic.kv.KeyValueListEnum
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.basic.PictureSelectionModel
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.style.PictureSelectorStyle

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/3/1
 *@time: 14:32
 *@description：
 */
class PictureSelectionUtil {
    companion object {
        fun openPhoto(
            context: Context,
            @KeyValueListEnum
            type: Int = KeyValueListEnum.ALL,
            maxSelectNumber: Int = 8,
            selectedList: List<LocalMedia>? = null,
            call: OnResultCallbackListener<LocalMedia>,
        ) {
            var gallery: Int = SelectMimeType.ofAll()
            if (type == KeyValueListEnum.IMG) {
                gallery = SelectMimeType.ofImage()
            } else if (type == KeyValueListEnum.VIDEO) {
                gallery = SelectMimeType.ofVideo()
            }
            // 进入相册
            val selectionModel: PictureSelectionModel = PictureSelector.create(context)
                //模式
                .openGallery(gallery)
                //样式
                .setSelectorUIStyle(PictureSelectorStyle())
                //加载驱动
                .setImageEngine(GlideEngine.createGlideEngine())
                //裁剪驱动
                .setCropEngine(null)
                //压缩驱动
                .setCompressEngine(ImageCompressEngine())
                //自定义沙箱驱动
                .setSandboxFileEngine(MeSandboxFileEngine())
                //自定义相机事件
                .setCameraInterceptListener(null)
                //拦截自定义提示
                .setSelectLimitTipsListener(MeOnSelectLimitTipsListener())
                //自定义编辑事件
                .setEditMediaInterceptListener(null) //.setExtendLoaderEngine(getExtendLoaderEngine())
                //注入自定义布局
                .setInjectLayoutResourceListener(null)
                //是否多选
                .setSelectionMode(SelectModeConfig.MULTIPLE)
                //语言
                .setLanguage(LanguageConfig.CHINESE)
                //降序升序查询
                .setQuerySortOrder("")
                //使用自定义路径 Camera
                .setOutputCameraDir("")
                //使用自定义路径 Audio
                .setOutputAudioDir("")
                //使用自定义路径  Sandbox
                .setQuerySandboxDir("")
                //显示资源时间轴
                .isDisplayTimeAxis(true)
                //查询指定目录
                .isOnlyObtainSandboxDir(false)
                //分页模式
                .isPageStrategy(true)
                //是否开启原图功能
                .isOriginalControl(false)
                //显示or隐藏拍摄
                .isDisplayCamera(true)
                //是否开启点击声音
                .isOpenClickSound(false)
                //跳过裁剪gif
                .setSkipCropMimeType(null)
                //滑动选择
                .isFastSlidingSelect(true)
                //.setOutputCameraImageFileName("luck.jpeg")
                //.setOutputCameraVideoFileName("luck.mp4")
                .setFilterVideoMaxSecond(31)
                .setFilterVideoMinSecond(2)
                .setSelectMaxDurationSecond(30)
                .setRecordVideoMaxSecond(30)
                .setFilterMaxFileSize((1024 * 100).toLong())
//                .setVideoQuality(0)
                //图片视频同选
                .isWithSelectVideoImage(true)
                //全屏预览(点击)
                .isPreviewFullScreenMode(true)
                //预览缩放效果
                .isPreviewZoomEffect(true)
                //是否预览图片
                .isPreviewImage(true)
                //是否预览视频
                .isPreviewVideo(true)
                //是否预览音频
                .isPreviewAudio(true) //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
                //是否显示蒙层(达到最大可选数量)
                .isMaxSelectEnabledMask(true)
                //单选模式直接返回
                .isDirectReturnSingle(false)
                //最大选择数量
                .setMaxSelectNum(maxSelectNumber)
                //展示动画
                .setRecyclerAnimationMode(AnimationType.SLIDE_IN_BOTTOM_ANIMATION)
                //要不要gif
                .isGif(false)
                //已选择的数据
                .setSelectedData(selectedList)
            selectionModel.forResult(call)
        }


    }
}