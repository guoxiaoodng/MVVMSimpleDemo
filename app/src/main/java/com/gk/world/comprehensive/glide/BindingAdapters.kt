package com.gk.world.comprehensive.glide

import android.graphics.Bitmap
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.videoFrameMillis
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.gk.world.resouce.dynamic.kv.KeyValueSingleView
import com.gk.world.resouce.dynamic.kv.KeyValueTwoLineView
import com.gk.world.resource.R

/**
 *版权 ： gxd
 *@author: Gk
 *@date: 2022/2/9
 *@time: 10:34
 *@description：imageview 设置图片工具类
 */
@BindingAdapter(
    value = ["imageUrl", "placeholder", "error", "fallback", "loadWidth", "loadHeight", "cacheEnable"],
    requireAll = false
)
fun setImageUrl(
    view: ImageView,
    source: String? = null,
    @DrawableRes
    placeholder: Int = R.drawable.image_placeholder,
    @DrawableRes
    error: Int = R.drawable.image_error,
    @DrawableRes
    fallback: Int = R.drawable.image_placeholder,
    width: Int? = -1,
    height: Int? = -1,
    cacheEnable: Boolean? = false,
) {
    if (!source.isNullOrEmpty()) {
        // 计算位图尺寸，如果位图尺寸固定，加载固定大小尺寸的图片，如果位图未设置尺寸，那就加载原图，Glide加载原图时，override参数设置 -1 即可。
        val widthSize = (if ((width ?: 0) > 0) width else view.width) ?: -1
        val heightSize = (if ((height ?: 0) > 0) height else view.height) ?: -1
        // 根据定义的 cacheEnable 参数来决定是否缓存
        val diskCacheStrategy =
            if (cacheEnable == true) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE
        // 设置编码格式，在Android 11(R)上面使用高清无损压缩格式 WEBP_LOSSLESS ， Android 11 以下使用PNG格式，PNG格式时会忽略设置的 quality 参数。
        val encodeFormat =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG

        if (source.endsWith(".mp4", true)) {
            view.load(source.replace("\\", "/")) {
                videoFrameMillis(1)
                placeholder(placeholder)
                error(error)
                fallback(fallback)
                RequestOptions.skipMemoryCacheOf(false)
            }
        } else {
            Glide.with(view.context)
                .asDrawable()
                .load(source.replace("\\", "/"))
                .placeholder(placeholder)
                .error(error)
                .fallback(fallback)
                .thumbnail()
                .override(widthSize, heightSize)
                .skipMemoryCache(false)
//        .sizeMultiplier(0.5f)
//        .format(DecodeFormat.PREFER_ARGB_8888)
                .encodeFormat(encodeFormat)
                .encodeQuality(80)
                .diskCacheStrategy(diskCacheStrategy)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view)
        }
    }
}

@BindingAdapter(
    value = ["value_text"],
    requireAll = false
)
fun setKeyValue(
    view: KeyValueSingleView,
    source: String? = null,
) {
    if (!source.isNullOrEmpty()) {
        view.setValueText(it = source)
    }
}

@BindingAdapter(
    value = ["value_text"],
    requireAll = false
)
fun setKeyValue1(
    view: KeyValueTwoLineView,
    source: String? = null,
) {
    if (!source.isNullOrEmpty()) {
        view.setValueText(it = source)
    }
}
