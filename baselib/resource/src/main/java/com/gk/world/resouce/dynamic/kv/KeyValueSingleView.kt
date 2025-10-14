package com.gk.world.resouce.dynamic.kv

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.gk.world.resouce.extend.StringExtend
import com.gk.world.resource.R
import timber.log.Timber


/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/10
 *@time: 14:09
 *@description：键值对  view 左边是列名称 右边是列值 eg: 名称   王XX
 * 1.kv  都要支持 设置drawable
 * 2.value 是edit text 可以编辑或不编辑
 */
class KeyValueSingleView : LinearLayoutCompat, View.OnClickListener {
    private var keyView: AppCompatTextView? = null
    private var valueView: AppCompatEditText? = null
    private var mL: KeyValueClickListener? = null
    private var division: View? = null
    private var star: AppCompatTextView? = null


    /*必传 加星标识*/
    private var showStar = 1

    /*是否是编辑*/
    private var isEdit = false


    /*脱敏*/
    private var isDesensitize = false

    /*值的权重*/
    private var valueGravity = 1

    /*数据对象的类型*/
    @KeyValueEnum
    private var mType = KeyValueEnum.ANY

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
                R.layout.key_value_single_layout,
                this, false
            )
            keyView = view?.findViewById(R.id.key)
            valueView = view?.findViewById(R.id.value)
            star = view?.findViewById(R.id.star)
            division = view?.findViewById(R.id.division)
            addView(view)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun initAttrs(attrs: AttributeSet) {
        try {
            val ta = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.KeyValueSingleView, 0, 0
            )
            val kt = ta.getString(R.styleable.KeyValueSingleView_key_text)
            val maxLength = ta.getInteger(R.styleable.KeyValueSingleView_value_max_length, 50)
            val ktc = ta.getColor(R.styleable.KeyValueSingleView_key_color, Color.BLACK)
            val kts = ta.getDimension(R.styleable.KeyValueSingleView_key_size, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics))
            val ktDrawableLeft = ta.getDrawable(R.styleable.KeyValueSingleView_key_drawable_left)
            val ktDrawableRight = ta.getDrawable(R.styleable.KeyValueSingleView_key_drawable_right)
            val vtHint = ta.getString(R.styleable.KeyValueSingleView_value_hint)
            val vtc = ta.getColor(R.styleable.KeyValueSingleView_value_color, Color.BLACK)
            val vts = ta.getDimension(R.styleable.KeyValueSingleView_value_size, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics))
            val vtDrawable = ta.getDrawable(R.styleable.KeyValueSingleView_value_drawable)
            val vtDrawableLeft = ta.getDrawable(R.styleable.KeyValueSingleView_value_drawable_left)
            mType = ta.getInt(R.styleable.KeyValueSingleView_value_type, KeyValueEnum.ANY)
            isEdit = ta.getBoolean(R.styleable.KeyValueSingleView_is_edit, false)
            isDesensitize = ta.getBoolean(R.styleable.KeyValueSingleView_desensitize, false)
            valueGravity = ta.getInt(R.styleable.KeyValueSingleView_value_gravity, 1)
            val valueSinge = ta.getBoolean(R.styleable.KeyValueSingleView_value_single_line, false)

            val showDivision = ta.getBoolean(R.styleable.KeyValueSingleView_show_division, false)
            showStar = ta.getInt(R.styleable.KeyValueSingleView_show_star, 1)
            val inputType = ta.getInt(R.styleable.KeyValueSingleView_value_inputType,
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            division?.visibility = if (showDivision) View.VISIBLE else View.GONE
            valueView?.inputType = inputType
            setStarShow()
            setKeyText(kt)
            setKeyColor(ktc)
            setKeySize(kts)
            ktDrawableLeft?.let { setKeyDrawableLeft(it) }
            ktDrawableRight?.let { setKeyDrawableRight(it) }
            setValueGravity(valueGravity)
            setValueText("", vtHint)
            setValueColor(vtc)
            setValueSize(vts)
            setValueMaxLength(maxLength)
            vtDrawable?.let { setValueDrawableRight(it) }
            vtDrawableLeft?.let { setValueDrawableLeft(it) }
            isEdit()
            if (isEdit) {
                valueView?.setOnClickListener(this)
                keyView?.setOnClickListener(this)
            }
            if (valueSinge) {
                valueView?.setSingleLine()
                valueView?.minLines = 1
                valueView?.background = null
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    private fun setValueMaxLength(maxLength: Int) {
        valueView?.filters = arrayOf<InputFilter>(object : LengthFilter(maxLength) {})
    }


    private fun setValueGravity(valueGravity: Int) {
        if (valueGravity == 0) {
            val mGravity = Gravity.START or Gravity.CENTER_VERTICAL
            valueView?.gravity = mGravity
        } else {
            val mGravity = Gravity.END or Gravity.CENTER_VERTICAL
            valueView?.gravity = mGravity
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


    fun addClickListener(l: KeyValueClickListener) {
        mL = l
    }

    /**
     * 是否可以编辑
     */
    fun isEdit() {
        valueView?.isFocusable = isEdit
        valueView?.isFocusableInTouchMode = isEdit
        valueView?.isEnabled = isEdit
        valueView?.isClickable = isEdit
        isFocusable = isEdit
        isFocusableInTouchMode = isEdit
        isEnabled = isEdit
        isClickable = isEdit
    }

    /**
     * 是否脱敏
     */
    fun desensitize(desensitize: Boolean) {
        this.isDesensitize = desensitize
    }

    /**
     * 设置 值的hint
     *这里根据type不同控制数据怎么展示  eg：  坐标 是json String  要解析成固定的值展示
     * type 参考 [KeyValueEnum] [R.styleable.KeyValueSingleView] value_type 属性
     */
    fun setValueText(
        it: String? = null,
        vH: String? = null,
    ) {
        try {
            when (mType) {
                KeyValueEnum.ANY -> {
                }
                KeyValueEnum.PHONE -> {
                    if (isEdit) {
                        valueView?.filters = arrayOf<InputFilter>(LengthFilter(11))
                        valueView?.inputType = EditorInfo.TYPE_CLASS_NUMBER
                    }
                }
                KeyValueEnum.IDENTITY -> {
                    if (isEdit) {
                        valueView?.filters = arrayOf<InputFilter>(LengthFilter(18))
                        valueView?.inputType = EditorInfo.TYPE_CLASS_NUMBER
                        valueView?.keyListener =
                            DigitsKeyListener.getInstance(context.getString(R.string.identity))
                    }
                }
                KeyValueEnum.DATE -> {
                }
                KeyValueEnum.SELECT_RES -> {
                }
                KeyValueEnum.LOCAL -> {
                }
                else -> {

                }
            }
            if (it.isNullOrEmpty()) {
                vH?.let { setValueHint(vH) }
                return
            }
            isEdit()
            if (isEdit) {
                valueView?.setText(it)
            } else {
                if (isDesensitize) {
                    valueView?.setText(StringExtend.stringDesensitize(it))
                } else {
                    valueView?.setText(it)
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    /**
     * 设置value的drawable Right
     * 业务中非编辑状态 才会去设置 此值
     */
    fun setValueDrawableRight(@Nullable d: Drawable) {
        valueView?.compoundDrawablePadding = 10
        valueView?.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null)
    }

    /**
     * 设置value的drawable Right
     * 业务中非编辑状态 才会去设置 此值
     */
    fun setValueDrawableLeft(@Nullable d: Drawable) {
        valueView?.compoundDrawablePadding = 10
        valueView?.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
    }

    /**
     * 设置value的颜色
     */
    fun setValueColor(@ColorInt color: Int) {
        valueView?.setTextColor(color)
    }

    /**
     * 设置value的颜色
     */
    fun setValueSize(size: Float) {
        valueView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    /**
     * 设置 值的hint
     */
    fun setValueHint(it: String) {
        valueView?.hint = it
    }


    /**
     * 设置key的值
     */
    fun setKeyText(text: String? = "") {
        keyView?.text = text
    }

    /**
     * 设置key的颜色
     */
    fun setKeyColor(@ColorInt color: Int) {
        keyView?.setTextColor(color)
    }

    /**
     * 设置key的颜色
     */
    fun setKeySize(size: Float) {
        keyView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    /**
     * 设置key的drawable
     */
    fun setKeyDrawableLeft(@Nullable d: Drawable) {
        keyView?.setCompoundDrawables(d, null, null, null)
    }

    /**
     * 设置key的drawable
     */
    fun setKeyDrawableRight(@Nullable d: Drawable) {
        keyView?.setCompoundDrawables(null, null, d, null)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.value, R.id.key -> {
                when (mType) {
                    KeyValueEnum.ANY -> {
                        mL?.addAnyClick(isEdit)
                    }
                    KeyValueEnum.PHONE -> {
                        mL?.addPhoneClick(isEdit)
                    }
                    KeyValueEnum.IDENTITY -> {
                        mL?.addIdentityClick(isEdit)
                    }
                    KeyValueEnum.DATE -> {
                        mL?.addDateClick(isEdit)
                    }
                    KeyValueEnum.SELECT_RES -> {
                        mL?.addSelectResClick(isEdit)
                    }
                    KeyValueEnum.LOCAL -> {
                        mL?.addLocalClick(isEdit)
                    }
                    else -> {

                    }
                }
            }
            else -> {

            }
        }
    }
}