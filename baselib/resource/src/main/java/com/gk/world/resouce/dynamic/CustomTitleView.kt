package com.gk.world.resouce.dynamic

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.gk.world.resouce.arouter.constance.ARouterConstance
import com.gk.world.resouce.extend.ActivityUtilsExtend
import com.gk.world.resource.R

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2021/11/9
 *@time: 15:55
 *@description： 自定义title view
 */
class CustomTitleView() : ConstraintLayout,
    View.OnClickListener, View.OnLongClickListener, Parcelable {

    private var arrow: AppCompatImageView? = null
    private var titleView: AppCompatTextView? = null
    private var rightTextView: AppCompatTextView? = null
    private var rightImgView: AppCompatImageView? = null
    private var rightImgView2: AppCompatImageView? = null
    private var operateLayout: LinearLayoutCompat? = null

    constructor(parcel: Parcel) : this() {

    }

    /*var rightText: String? = null
    var tintImg: Drawable? = null*/

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
     * set right view
     * 需要自定义布局用这个
     */
    fun setRightView(@LayoutRes resource: Int, vararg id: Int, listener: OnClickListener) {
        val rightView = LayoutInflater.from(context).inflate(resource, this, false)
        operateLayout?.addView(rightView)
        id.forEach { rightView.findViewById<View>(it).setOnClickListener(listener) }
    }


    /**
     * set right img
     */
    fun setRightImage(@DrawableRes resId: Int, listener: OnClickListener) {
        rightImgView?.visibility = View.VISIBLE
        rightImgView?.setImageResource(resId)
        rightImgView?.setOnClickListener(listener)
    }

    fun setRightImage2(@DrawableRes resId: Int, listener: OnClickListener) {
        rightImgView2?.visibility = View.VISIBLE
        rightImgView2?.setImageResource(resId)
        rightImgView2?.setOnClickListener(listener)
    }

    /**
     * set right text
     */
    fun setRightText(
        @StringRes resId: Int,
        @ColorInt color: Int = resources.getColor(R.color.theme_color),
        listener: OnClickListener,
    ) {
        setRightText(context.getString(resId), color, listener)
    }

    /**
     * set right text
     */
    fun setRightText(
        res: String,
        @ColorInt color: Int = resources.getColor(R.color.theme_color),
        listener: OnClickListener,
    ) {
        rightTextView?.visibility = View.VISIBLE
        rightTextView?.text = res
        rightTextView?.setTextColor(color)
        rightTextView?.setOnClickListener(listener)
    }


    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.title_common, this, false)
        arrow = view.findViewById(R.id.arrow)
        titleView = view.findViewById(R.id.title)
        rightTextView = view.findViewById(R.id.right_text)
        rightImgView = view.findViewById(R.id.right_img)
        rightImgView2 = view.findViewById(R.id.right_img_2)
        titleView?.setOnClickListener(this)
        arrow?.setOnClickListener(this)
        titleView?.setOnLongClickListener(this)
        arrow?.setOnLongClickListener(this)
        operateLayout = view.findViewById(R.id.operate_layout)
        addView(view)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val ta = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTitleView, 0, 0
        )
        val title = ta.getString(R.styleable.CustomTitleView_title)
        val tintColor = ta.getColor(R.styleable.CustomTitleView_tint_color, Color.BLACK)
        /*rightText = ta.getNonResourceString(R.styleable.CustomTitleView_right_text)
        tintImg = ta.getDrawable(R.styleable.CustomTitleView_tint_img)*/
        titleView?.text = title
        titleView?.setTextColor(tintColor)
        arrow?.setColorFilter(tintColor)
    }

    fun setTitle(title: String) {
        titleView?.text = title
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.title, R.id.arrow -> {
                val activity = context as? Activity
                activity?.onBackPressed()
            }
            else -> {

            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.title, R.id.arrow -> {
                ActivityUtilsExtend.finishOtherWithNameActivities(ARouterConstance.Main.MAIN_ACTIVITY)
            }
            else -> {

            }
        }
        return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomTitleView> {
        override fun createFromParcel(parcel: Parcel): CustomTitleView {
            return CustomTitleView(parcel)
        }

        override fun newArray(size: Int): Array<CustomTitleView?> {
            return arrayOfNulls(size)
        }
    }
}