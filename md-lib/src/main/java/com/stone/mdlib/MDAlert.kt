package com.stone.mdlib

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView

class MDParams {

    var title: CharSequence = ""
    var message: CharSequence = ""
    var btnNegativeText: CharSequence = ""
    var btnPositiveText: CharSequence = ""
    var btnNeutralText: CharSequence = ""
    /**
     * 设置为false并且title为空时，才可以隐藏标题
     */
    var isShowTitle: Boolean = true
    /**
     * 点击back是否可取消
     */
    var cancelable = true

    var listener: DialogInterface.OnClickListener? = null//所有按钮的监听
    var positiveListener: DialogInterface.OnClickListener? = null
    var negativeListener: DialogInterface.OnClickListener? = null
    var neutralListener: DialogInterface.OnClickListener? = null

    var showListener: DialogInterface.OnShowListener? = null

    /**
     * 可自定义 WindowAnimations
     */
    @StyleRes
    var animRes: Int = 0

    @StyleRes
    var alertStyleRes: Int = 0
}

private fun Context.dp2px(dp: Int): Int {
    val ds = resources.displayMetrics.density
    return (dp * ds + 0.5f * (if (dp >= 0) 1 else -1)).toInt()
}

/**
 * Created By: sqq
 * Created Time: 17/6/20 下午2:19.
 * 针对系统弹框[AlertDialog]的自定义处理封装
 *
 * 功能简介：
 * 1. 可自定义字体大小、颜色（包括title、Message、Button）；
 * 2. 可自定义按钮的事件，包括Alert的dismiss也可以通过自定义
 * 3. 可自定义ContentView，支持内容滑动的包装
 * 4. 可自定义TitleView
 * 5. 默认的文案，颜色，字体大小可支持APP全局配置
 */
open class MDAlert(protected val ctx: Context) {
    protected val p = MDParams()

    var customView: View? = null
        private set
    private var titleView: View? = null


    var dialog: AlertDialog? = null
        private set//实际show出来的对象

    private lateinit var containerView: FrameLayout
    private var rootView: View? = null

    /**
     * 单确定按钮
     */
    constructor(ctx: Context, message: CharSequence) : this(ctx) {
        p.message = message
    }

    constructor(ctx: Context, @StyleRes alertStyle: Int) : this(ctx) {
        p.alertStyleRes = alertStyle
//        println("constructor ....")
    }

    fun create(): MDAlert {
        if (p.isShowTitle && p.title.isEmpty()) {
            setTitle(defaultTitle)
        }
        if (p.btnPositiveText.isEmpty()) setBtnPositive(defaultPositiveText)

        if (p.listener != null) {
            p.positiveListener = p.listener
            p.negativeListener = p.listener
            p.neutralListener = p.listener
        }
        val builder = AlertDialog.Builder(ctx, if (p.alertStyleRes == 0) defaultAlertStyle else p.alertStyleRes)

        if (titleView == null) {
            builder.setTitle(p.title)
        } else {
            builder.setCustomTitle(titleView)
            (titleView?.findViewById<View>(R.id.title) as TextView).text = p.title
        }

        if (rootView == null) {
            builder.setMessage(p.message)
        } else {
            builder.setView(rootView)
        }

        dialog = builder
                .setPositiveButton(p.btnPositiveText, p.positiveListener)
                .setNegativeButton(p.btnNegativeText, p.negativeListener)
                .setNeutralButton(p.btnNeutralText, p.neutralListener)
                .create()

        if (p.showListener != null) {
            dialog?.setOnShowListener(p.showListener)
        }

        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(p.cancelable)
        return this
    }

    fun show(): MDAlert {
        if (dialog == null) {
            create()
        }

        try {
            dialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
            return this
        }

        val window = dialog?.window
        if (window != null) {
            window.setLayout(-1, -2)
            if (p.animRes != 0) {
                window.setWindowAnimations(p.animRes)
            }
        }
        for (i in -3..-1) {
            val button = dialog?.getButton(i)
            if (button != null) {
                //                button.setAllCaps(false);
                button.setTypeface(null, Typeface.NORMAL)
//                Logs.i(TAG, "show:" + i + ":::::" + button.paddingLeft + ",," + button.paddingTop + ",," + button.paddingRight + ",," + button.paddingBottom)
                if (button.paddingLeft == 0) {
                    button.setPadding(ctx.dp2px(25), 0, ctx.dp2px(5), 0)
                }
            }
        }
        return this
    }


    fun dismiss() {
        if (dialog != null) {
            try {
                dialog?.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Log.e(TAG, "dismiss: the dialog is null when dismiss in the MDAlert ")
        }
    }


    /**
     * @param whichButton -1 Positive -2 negative -3 neutral
     * @return
     */
    fun getButton(whichButton: Int): Button? {
        return if (dialog != null) {
            dialog?.getButton(whichButton)
        } else null
    }

    fun setTitleView(view: View): MDAlert {
        this.titleView = view
        return this
    }

    fun setTitleView(@LayoutRes res: Int): MDAlert {
        this.titleView = LayoutInflater.from(ctx).inflate(res, null)
        return this
    }

    //    public MDAlert setDefaultCustomTitle() {
    //        return setTitleView(R.layout.dlg_default_custom_title);
    //    }

    /**
     * 设置默认的negative按钮
     *
     * 使用场景：
     * 用以快速设置"取消"按钮，即：双按钮的Alert
     *
     * 默认的按钮文案，取决于Companion中default系列属性
     *
     */
    fun setCancel(): MDAlert {
        return setBtnNegative(defaultNegativeText)
    }

    /**
     * 自定义View
     *
     * 注意：本封装中会默认在此View的基础上添加统一的父容器（即：rootView或者containerView），最终设置给[AlertDialog.Builder.setView]的View是rootView的值
     */
    @JvmOverloads
    fun setCustomView(@LayoutRes res: Int, isWrapScrolled: Boolean = false): MDAlert {
        val view = LayoutInflater.from(ctx).inflate(res, null)
        setCustomView(view, isWrapScrolled)
        return this
    }

    /**
     * 自定义View
     *
     * 注意：本封装中会默认在此View的基础上添加统一的父容器（即：rootView或者containerView），最终设置给[AlertDialog.Builder.setView]的View是rootView的值
     */
    fun setCustomView(view: View, isWrapScrolled: Boolean): MDAlert {
        this.customView = view
        createRootContent(isWrapScrolled)
        containerView.addView(view)
        return this
    }

    private fun createRootContent(isWrapScrolled: Boolean) {
        containerView = FrameLayout(ctx)
        containerView.setPadding(ctx.dp2px(25), ctx.dp2px(10), ctx.dp2px(25), ctx.dp2px(10))
        containerView.layoutParams = FrameLayout.LayoutParams(-1, -1)
        rootView = containerView
        if (isWrapScrolled) {
            val scrollView = ScrollView(ctx)
            scrollView.addView(containerView, FrameLayout.LayoutParams(-1, -2))
            rootView = scrollView
        }
    }

    /**
     * 自定义 Root 的样式
     * 直接设置对应系统API中的[AlertDialog.Builder.setView]
     */
    fun setRootView(view: View): MDAlert {
        this.rootView = view
        this.customView = view
        return this
    }

    fun setNoTitle(): MDAlert {
        p.isShowTitle = false
        return this
    }

    fun setAnimationsRes(@StyleRes res: Int): MDAlert {
        p.animRes = res
        return this
    }

    /**
     * @param message msg
     * @param color   colorInt，默认值-1，即不处理颜色，跟随系统
     * @param size    字体大小dp ，默认值-1，跟随系统
     * @param isBold 是否加粗，默认false
     */
    @JvmOverloads
    fun setMessage(message: CharSequence, @ColorInt color: Int = defaultMessageColor, size: Int = defaultMessageSize, isBold: Boolean = false): MDAlert {
        val tempMsg = setText(message, color, size, isBold)
        p.message = if (isBold) getBoldString(tempMsg) else tempMsg
        return this
    }

    fun setMessage(@StringRes message: Int): MDAlert {
        return setMessage(ctx.getString(message))
    }

    /**
     * @param title title
     * @param color   colorInt，默认值-1，即不处理颜色，跟随系统
     * @param size    字体大小dp ，默认值-1，跟随系统
     * @param isBold 是否加粗，默认false
     */
    @JvmOverloads
    fun setTitle(title: CharSequence, @ColorInt color: Int = defaultTitleColor, size: Int = defaultTitleSize, isBold: Boolean = false): MDAlert {
        val temp = setText(title, color, size, isBold)
        p.title = if (isBold) getBoldString(temp) else temp
        return this
    }

    fun setTitle(@StringRes title: Int): MDAlert {
        return setTitle(ctx.getString(title))
    }

    /**
     * @param text text
     * @param color   colorInt，默认值-1，即不处理颜色，跟随系统
     * @param size    字体大小dp ，默认值-1，跟随系统
     * @param isBold 是否加粗，默认false
     */
    @JvmOverloads
    fun setBtnPositive(text: CharSequence, @ColorInt color: Int = defaultPositiveColor, size: Int = defaultBtnSize, isBold: Boolean = false): MDAlert {
        val temp = setText(text, color, size, isBold)
        p.btnPositiveText = if (isBold) getBoldString(temp) else temp
        return this
    }

    fun setBtnPositive(@StringRes text: Int): MDAlert {
        return setBtnPositive(ctx.getString(text))
    }

    /**
     * @param text text
     * @param color   colorInt，默认值-1，即不处理颜色，跟随系统
     * @param size    字体大小dp ，默认值-1，跟随系统
     * @param isBold 是否加粗，默认false
     */
    @JvmOverloads
    fun setBtnNegative(text: CharSequence, @ColorInt color: Int = defaultNegativeColor, size: Int = defaultBtnSize, isBold: Boolean = false): MDAlert {
        val temp = setText(text, color, size, isBold)
        p.btnNegativeText = if (isBold) getBoldString(temp) else temp
        return this
    }

    fun setBtnNegative(@StringRes text: Int): MDAlert {
        return setBtnNegative(ctx.getString(text))
    }


    /**
     * @param text text
     * @param color   colorInt，默认值-1，即不处理颜色，跟随系统
     * @param size    字体大小dp ，默认值-1，跟随系统
     * @param isBold 是否加粗，默认false
     */
    @JvmOverloads
    fun setBtnNeutral(text: CharSequence, @ColorInt color: Int = -1, size: Int = defaultBtnSize, isBold: Boolean = false): MDAlert {
        val temp = setText(text, color, size, isBold)
        p.btnNeutralText = if (isBold) getBoldString(temp) else temp
        return this
    }

    fun setBtnNeutral(@StringRes text: Int): MDAlert {
        return setBtnNegative(ctx.getString(text))
    }

    /**
     * 设置此总的监听会覆盖下面三个单独设置的监听
     */
    fun setListener(listener: DialogInterface.OnClickListener?): MDAlert {
        p.listener = listener
        return this
    }

    fun setPositiveListener(positiveListener: DialogInterface.OnClickListener?): MDAlert {
        p.positiveListener = positiveListener
        return this
    }

    fun setNegativeListener(negativeListener: DialogInterface.OnClickListener?): MDAlert {
        p.negativeListener = negativeListener
        return this
    }

    fun setNeutralListener(neutralListener: DialogInterface.OnClickListener?): MDAlert {
        p.neutralListener = neutralListener
        return this
    }

    fun setShowListener(showListener: DialogInterface.OnShowListener?): MDAlert {
        p.showListener = showListener
        return this
    }

    fun setCancelable(cancelable: Boolean): MDAlert {
        p.cancelable = cancelable
        return this
    }

    protected fun getColorSizeString(text: CharSequence, @ColorInt color: Int, size: Int): CharSequence {
        if (TextUtils.isEmpty(text)) return text
        val ss = SpannableString(text)
        ss.setSpan(ForegroundColorSpan(color), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(AbsoluteSizeSpan(size, true), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    protected fun getColorString(text: CharSequence, @ColorInt color: Int): CharSequence {
        if (TextUtils.isEmpty(text)) return text
        val ss = SpannableString(text)
        ss.setSpan(ForegroundColorSpan(color), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    protected fun getBoldString(text: CharSequence): CharSequence {
        if (TextUtils.isEmpty(text)) return text
        val ss = SpannableString(text)
        ss.setSpan(StyleSpan(Typeface.BOLD), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    protected fun getSizeString(text: CharSequence, size: Int): CharSequence {
        if (TextUtils.isEmpty(text)) return text
        if (size <= 0) return text
        val ss = SpannableString(text)
        ss.setSpan(AbsoluteSizeSpan(size, true), 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * @param text text
     * @param color   colorInt，默认值-1，即不处理颜色，跟随系统
     * @param size    字体大小dp ，默认值-1，跟随系统
     * @param isBold 是否加粗，默认false
     */
    private fun setText(text: CharSequence, @ColorInt color: Int = -1, size: Int = -1, isBold: Boolean = false): CharSequence {
        return when {
            size == -1 && color == -1 -> text
            size == -1 -> getColorString(text, color)
            color == -1 -> getSizeString(text, size)
            else -> getColorSizeString(text, color, size)
        }
    }

    companion object {

        private const val TAG = "MDAlert"

        /**
         * Button的默认字体大小，单位dp
         */
        var defaultBtnSize = 18

        /**
         * title默认字体大小，单位dp
         */
        var defaultTitleSize = 18

        /**
         * 默认不修改内容文字大小
         */
        var defaultMessageSize = -1

        @ColorInt
        var defaultTitleColor = -1

        /**
         * 设置当前MD默认Positive按钮文字颜色
         *
         * 默认值为-1，效果是跟随APP主题配置的颜色，即不做颜色修改设置；
         * 当值不为-1时，颜色应用当前设置的值
         */
        @ColorInt
        var defaultPositiveColor = -1

        @ColorInt
        var defaultMessageColor = -1

        @ColorInt
        var defaultNegativeColor = Color.parseColor("#444444")

        var defaultPositiveText = "确定"
        var defaultNegativeText = "取消"

        var defaultTitle = "提示："

        @StyleRes
        var defaultAlertStyle = R.style.MDAlertStyle
    }

}
