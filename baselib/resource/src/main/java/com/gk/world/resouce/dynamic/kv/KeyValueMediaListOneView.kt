package com.gk.world.resouce.dynamic.kv

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.videoFrameMillis
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.*
import com.drake.net.Post
import com.drake.net.utils.scopeDialog
import com.gk.world.net.constance.http.HttpPortConst
import com.gk.world.net.converter.GsonConverter
import com.gk.world.resouce.arouter.ARouterUtils
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.bean.KeyValueAddInterface
import com.gk.world.resouce.bean.KeyValueInterface
import com.gk.world.resouce.bean.UploadDataBean
import com.gk.world.resouce.engine.PictureSelectionUtil
import com.gk.world.resource.R
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.PictureFileUtils
import com.tecent.tecentx5.bean.MediaBean
import timber.log.Timber
import java.io.File
import java.util.*


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/10
 *@time: 14:09
 *@description：与 KeyValueMediaListView 的不同是 删除方式不同 这里是点击❌删除
 * 键值对  view 左边是列名称 右边是列值 eg: 名称   王XX
 * 1.kv  都要支持 设置drawable
 * 2.value 多张图片
 */
class KeyValueMediaListOneView : LinearLayoutCompat {
    var keyView: AppCompatTextView? = null
    var fileRv: RecyclerView? = null
    private var division: View? = null
    private var star: AppCompatTextView? = null

    /*是否是编辑*/
    var isEdit = false

    @SuppressLint("UseCompatLoadingForDrawables")
    var mDr: Drawable = context.resources.getDrawable(R.drawable.add_blue)


    /*数据对象的类型*/
    @KeyValueListEnum
    var mType = KeyValueListEnum.ALL

    var divider = 20
    var spanCount = 4
    var maxSelectNumber = 3

    /*必传 加星*/
    private var showStar = 1

    private var uploadType = ""

    private var uploadUrl = ""

    /**已选择的媒体资源*/
    var mResult = arrayListOf<LocalMedia>()

    var removePosition = -1

    interface OnValueChangedListener {
        fun onValueChanged(values: List<UploadDataBean>?)
    }

    var valueChangedListener: OnValueChangedListener? = null


    constructor(@NonNull context: Context) : this(
        context,
        null
    )


    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet? = null) : this(
        context,
        attrs,
        0
    )

    constructor(
        @NonNull context: Context,
        @Nullable attrs: AttributeSet? = null,
        defStyleAttr: Int,
    ) : super(
        context, attrs, defStyleAttr
    ) {
        initView()
        if (attrs != null) {
            initAttrs(attrs)
        }
    }

    /**
     * 初始化布局
     */
    private fun initView() {
        try {
            val view = LayoutInflater.from(context).inflate(
                R.layout.key_value_recycler_layout,
                this, false
            )
            addView(view)
            keyView = view.findViewById(R.id.key)
            fileRv = view.findViewById(R.id.file_rv)
            star = view.findViewById(R.id.star)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun setMaxSelect(num: Int) {
        maxSelectNumber = num
    }

    fun setOnValueChangedListener(onValueChangedListener: OnValueChangedListener) {
        this.valueChangedListener = onValueChangedListener
    }

    private fun initAttrs(attrs: AttributeSet) {
        try {
            val ta = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.KeyValueRecyclerView, 0, 0
            )
            val kt = ta.getString(R.styleable.KeyValueRecyclerView_key_text)
            val ktc = ta.getColor(R.styleable.KeyValueRecyclerView_key_color, Color.BLACK)
            val kts = ta.getDimension(R.styleable.KeyValueRecyclerView_key_size, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics))
            val ktDrawableLeft = ta.getDrawable(R.styleable.KeyValueRecyclerView_key_drawable_left)
            val ktDrawableRight =
                ta.getDrawable(R.styleable.KeyValueRecyclerView_key_drawable_right)
            mType = ta.getInt(R.styleable.KeyValueRecyclerView_value_type, KeyValueListEnum.ALL)
            isEdit = ta.getBoolean(R.styleable.KeyValueRecyclerView_is_edit, false)
            divider = ta.getInt(R.styleable.KeyValueRecyclerView_grid_divider, 20)
            spanCount = ta.getInt(R.styleable.KeyValueRecyclerView_grid_span_count, 4)
            maxSelectNumber = ta.getInt(R.styleable.KeyValueRecyclerView_max_select_number, 3)
            val showDivision = ta.getBoolean(R.styleable.KeyValueRecyclerView_show_division, false)
            division?.visibility = if (showDivision) View.VISIBLE else View.GONE
            showStar = ta.getInt(R.styleable.KeyValueRecyclerView_show_star, 1)
            setStarShow()
            setKeyText(kt)
            setKeyColor(ktc)
            setKeySize(kts)
            ktDrawableLeft?.let { setKeyDrawableLeft(it) }
            if (isEdit) {
                setValue(listOf(UploadDataBean(drawable = R.drawable.up_f, add = true)))
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    /**
     * 设置 值的hint
     *这里根据type不同控制数据怎么展示  eg：  坐标 是json String  要解析成固定的值展示
     * type 参考 [KeyValueListEnum] [R.styleable.KeyValueRecyclerView] value_type 属性
     * 这里暂时定义为 json string
     */
    fun setValueString(
        data: List<String>,
    ) {
        try {
            if (data.isNullOrEmpty())
                return
            when (mType) {
                KeyValueListEnum.ALL, KeyValueListEnum.IMG, KeyValueListEnum.VIDEO -> {
                    if (fileRv!!.adapter != null) {
                        fileRv!!.models = data
                        return
                    }
                    fileRv!!.divider { // 构建间距
                        setDivider(divider)
                        includeVisible = true
                        orientation = DividerOrientation.GRID
                    }.grid(spanCount).setup {
                        addType<String>(R.layout.key_value_recycler_item)
                        onBind {
                            val img = findView<AppCompatImageView>(R.id.img)
                            val playIcon = findView<AppCompatImageView>(R.id.play_icon)
                            val model = getModel<String>().replace("\\", "/")
                            if (model.endsWith("mp4")) {
                                img.load(model) {
                                    videoFrameMillis(1)
                                    placeholder(R.drawable.image_placeholder)
                                    error(R.drawable.image_error)
                                }
                                playIcon.visibility = View.VISIBLE
                            } else {
                                img.load(model) {
                                    placeholder(R.drawable.image_placeholder)
                                    error(R.drawable.image_error)
//                                    transformations(CircleCropTransformation())
                                }
                                playIcon.visibility = View.GONE
                            }
                        }
                        onClick(R.id.item) {
                            val model = getModel<String>()
                            ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
                                null,
                                MediaBean(model.replace("\\", "/")),
                                activity = context as Activity
                            )
                        }
                    }.models = data
                }
                KeyValueListEnum.RECORD -> {
                    fileRv!!.divider { // 构建间距
                        setDivider(divider)
                        includeVisible = true
                        orientation = DividerOrientation.GRID
                    }.grid(spanCount, scrollEnabled = false).setup {
                        addType<String>(R.layout.key_value_recycler_item)
                        onBind {
                            findView<AppCompatImageView>(R.id.img).load(R.drawable.record_on)
                        }
                        onClick(R.id.item) {
                            val model = getModel<String>()
                            ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
                                null,
                                MediaBean(model.replace("\\", "/")),
                                activity = context as Activity
                            )
                        }
                    }.models = data
                }
                KeyValueListEnum.FILE -> {
                    fileRv!!.divider { // 构建间距
                        setDivider(divider)
                        includeVisible = true
                        orientation = DividerOrientation.GRID
                    }.grid(spanCount).setup {
                        addType<String>(R.layout.key_value_recycler_item)
                        onBind {
                            findView<AppCompatImageView>(R.id.img).load(R.drawable.file_icon)
                        }
                        onClick(R.id.item) {
                            val model = getModel<String>()
                            ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
                                null,
                                MediaBean(model.replace("\\", "/")),
                                activity = context as Activity
                            )
                        }

                    }.models = data
                }
                else -> {

                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    /**
     * 设置 值的hint
     *这里根据type不同控制数据怎么展示  eg：  坐标 是json String  要解析成固定的值展示
     * type 参考 [KeyValueListEnum] [R.styleable.KeyValueRecyclefileRview] value_type 属性
     * 这里暂时定义为 json string
     */
    inline fun <reified T : KeyValueAddInterface> setValue(
        data: List<T>? = null,
    ) {
        try {
            when (mType) {
                KeyValueListEnum.ALL, KeyValueListEnum.IMG, KeyValueListEnum.VIDEO -> {
                    fileRv!!.divider { // 构建间距
                        setDivider(divider)
                        includeVisible = true
                        orientation = DividerOrientation.GRID
                    }.grid(spanCount).setup {
                        addType<T>(R.layout.key_value_recycler_item)
                        onBind {
                            val img = findView<AppCompatImageView>(R.id.img)
                            val playIcon = findView<AppCompatImageView>(R.id.play_icon)
                            val delete = findView<AppCompatImageView>(R.id.delete)
                            val model = getModel<T>()
                            if (model.add()) {
                                img.load(model.drawable())
                                return@onBind
                            }
                            if (isEdit && !model.add()) {
                                delete.visibility = View.VISIBLE
                            } else {
                                delete.visibility = View.GONE
                            }
                            val value = model.getValue().replace("\\", "/")
                            if (value.endsWith("mp4")) {
                                img.load(value) {
                                    videoFrameMillis(1)
                                    placeholder(R.drawable.image_placeholder)
                                    error(R.drawable.image_error)
                                    transformations(RoundedCornersTransformation())
                                }
                                playIcon.visibility = View.VISIBLE
                            } else {
                                img.load(value) {
                                    placeholder(R.drawable.image_placeholder)
                                    error(R.drawable.image_error)
                                    transformations(RoundedCornersTransformation())
//                                    transformations(CircleCropTransformation())
                                }
                                playIcon.visibility = View.GONE
                            }
                            if (!getModel<T>().getKey().isNullOrEmpty()) {
                                findView<AppCompatTextView>(R.id.img_text).visibility = View.VISIBLE
                                findView<AppCompatTextView>(R.id.img_text).text =
                                    getModel<T>().getKey()
                            }
                        }
                        onClick(R.id.item) {
                            val model = getModel<T>()
                            if (model.add()) {
                                try {
                                    if (fileRv!!.models != null && fileRv!!.models?.size!! >= maxSelectNumber) {
                                        ToastUtils.showLong("最大上传数量为$maxSelectNumber")
                                        return@onClick
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                PictureSelectionUtil.openPhoto(context,
                                    mType,
                                    maxSelectNumber = maxSelectNumber,
                                    mResult,
                                    object :
                                        OnResultCallbackListener<LocalMedia> {
                                        override fun onResult(result: ArrayList<LocalMedia>?) {
                                            try {
                                                if (result != null) {
                                                    mResult = result
                                                }
                                                if (!result.isNullOrEmpty()) {
                                                    val strings: MutableList<String> =
                                                        ArrayList()
                                                    for (media in result) {
                                                        if (!media.compressPath.isNullOrEmpty()) {
                                                            strings.add(media.compressPath)
                                                        } else if (!media.sandboxPath.isNullOrEmpty()) {
                                                            strings.add(media.sandboxPath)
                                                        } else if (!media.realPath.isNullOrEmpty()) {
                                                            strings.add(media.realPath)
                                                        } else if (!media.path.isNullOrEmpty()) {
                                                            strings.add(PictureFileUtils.getPath(
                                                                context,
                                                                Uri.parse(media.path)))
                                                        }
                                                    }
                                                    val list = mutableListOf<File>()
                                                    strings.forEach {
                                                        list.add(File(it))
                                                    }
                                                    uploadMediaFile(list)
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }

                                        override fun onCancel() {}
                                    })
                            } else {
                                val value = model.getValue().replace("\\", "/")
                                ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW, null,
                                    MediaBean(value.replace("\\", "/")),
                                    activity = context as Activity
                                )
                            }
                        }
                    }
                        .models = data
                }
                KeyValueListEnum.RECORD -> {
                    fileRv!!.divider { // 构建间距
                        setDivider(divider)
                        includeVisible = true
                        orientation = DividerOrientation.GRID
                    }.grid(spanCount, scrollEnabled = false).setup {
                        addType<T>(R.layout.key_value_recycler_item)
                        onBind {
                            val img = findView<AppCompatImageView>(R.id.img)
                            img.load(R.drawable.record_on)
                            if (!getModel<T>().getKey().isNullOrEmpty()) {
                                findView<AppCompatTextView>(R.id.img_text).visibility = View.VISIBLE
                                findView<AppCompatTextView>(R.id.img_text).text =
                                    getModel<T>().getKey()
                            }
                            val delete = findView<AppCompatImageView>(R.id.delete)
                            val model = getModel<T>()
                            if (model.add()) {
                                img.load(model.drawable())
                                return@onBind
                            }
                            if (isEdit && !model.add()) {
                                delete.visibility = View.VISIBLE
                            } else {
                                delete.visibility = View.GONE
                            }
                        }
                        onClick(R.id.item) {
                            val model = getModel<T>()
                            if (model.add()) {
                                ToastUtils.showLong("添加录音文件 未实现")
                                return@onClick
                            }
                            val value = model.getValue().replace("\\", "/")

                            ToastUtils.showLong("播放录音文件 未实现")
                            /*ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
                                null,
                                MediaBean(value.replace("\\", "/")),
                                activity = context as Activity
                            )*/
                        }
                    }.models = data
                }
                KeyValueListEnum.FILE -> {
                    fileRv!!.divider { // 构建间距
                        setDivider(divider)
                        includeVisible = true
                        orientation = DividerOrientation.GRID
                    }.grid(spanCount).setup {
                        addType<T>(R.layout.key_value_recycler_item)
                        onBind {
                            val img = findView<AppCompatImageView>(R.id.img)
                            img.load(R.drawable.file_icon)
                            if (!getModel<T>().getKey().isNullOrEmpty()) {
                                findView<AppCompatTextView>(R.id.img_text).visibility = View.VISIBLE
                                findView<AppCompatTextView>(R.id.img_text).text =
                                    getModel<T>().getKey()
                            }
                            val delete = findView<AppCompatImageView>(R.id.delete)
                            val model = getModel<T>()
                            if (model.add()) {
                                img.load(model.drawable())
                                return@onBind
                            }
                            if (isEdit && !model.add()) {
                                delete.visibility = View.VISIBLE
                            } else {
                                delete.visibility = View.GONE
                            }
                        }
                        onClick(R.id.item) {
                            val model = getModel<T>()
                            if (model.add()) {
                                ToastUtils.showLong("去文件管理器获取文件，未实现")
                                return@onClick
                            }
                            val value = model.getValue().replace("\\", "/")
                            ARouterUtils.routerParcelable(ARouterConstance.X5.X5_WEB_VIEW,
                                null,
                                MediaBean(value),
                                activity = context as Activity
                            )
                        }

                    }.models = data
                }
            }
            if (isEdit)
                fileRv!!.bindingAdapter.onClick(R.id.delete) {
                    removePosition = modelPosition
                    adapter.mutable.removeAt(modelPosition)
                    adapter.notifyItemRemoved(modelPosition)
                    valueChangedListener?.onValueChanged(adapter.models as List<UploadDataBean>?)
                    if (mResult.size <= modelPosition) {
                        mResult.removeAt(modelPosition)
                    }
                }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    inline fun <reified T : KeyValueInterface> getValue(): List<T> {
        val list = mutableListOf<T>()
        if (fileRv!!.adapter != null) {
            fileRv!!.models?.forEach {
                if (it is T) {
                    list.add(it)
                }
            }
        }
        return list
    }


    /**上传文件*/
    public fun uploadMediaFile(fs: MutableList<File>) {
        (context as FragmentActivity).scopeDialog {
            Post<List<UploadDataBean>>(HttpPortConst.Java.UserCenter.SMART_PARK_UPLOAD_FILE) {
                //新增上传的时候model=workOrderAdd，处置上传时model=workOrderDispose
                converter = GsonConverter()
                param("files", fs)
                param("groupKey", "patrolModel")
                param("dirPath", "patrol")
            }.await().let {
                if (fileRv!!.adapter != null) {
                    fileRv!!.addModels(it)
                } else {
                    setValue(it)
                }
                valueChangedListener?.onValueChanged(it)
            }
        }
    }

    /**
     * 设置key的值
     */
    private fun setKeyText(text: String? = "") {
        if (StringUtils.isEmpty(text)) {
            keyView!!.visibility = View.GONE
        } else {
            keyView!!.visibility = View.VISIBLE
            keyView!!.text = text
        }
    }
    private fun setStarShow() {
        when (showStar) {
            0 -> {
                star?.visibility = View.VISIBLE
            }
            1 -> {
                star?.visibility = View.INVISIBLE
            }
            2 -> {
                star?.visibility = View.GONE
            }
        }
    }
    /**
     * 设置key的颜色
     */
    private fun setKeyColor(@ColorInt color: Int) {
        keyView!!.setTextColor(color)
    }

    /**
     * 设置key的颜色
     */
    private fun setKeySize(size: Float) {
        keyView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    /**
     * 设置key的drawable
     */
    private fun setKeyDrawableLeft(@Nullable d: Drawable) {
        keyView!!.setCompoundDrawables(d, null, null, null)
    }

    /**
     * 设置key的drawable
     */
    private fun setKeyDrawableRight() {
        mDr.setBounds(0, 0, mDr.minimumWidth, mDr.minimumHeight)
        keyView!!.setCompoundDrawables(null, null, mDr, null)
    }
}