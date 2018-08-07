//package com.stone.mdlib;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Typeface;
//import android.os.Build;
//import android.support.annotation.ColorInt;
//import android.support.annotation.ColorRes;
//import android.support.annotation.LayoutRes;
//import android.support.annotation.StringRes;
//import android.support.annotation.StyleRes;
//import android.support.v7.app.AlertDialog;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.style.AbsoluteSizeSpan;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.StyleSpan;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
///**
// * Created By: sqq
// * Created Time: 17/6/20 下午2:19.
// * <p>
// * 针对系统弹框{@link AlertDialog}的自定义处理封装
// */
//public class DialogMD {
//
//    //title
//    private CharSequence title;
//    //Button
//    private CharSequence btnPositiveText;
//    private CharSequence btnNegativeText;
//    private CharSequence btnNeutralText;
//    //Message
//    private CharSequence message;
//
//    private View customView;
//    private View titleView;
//
//    protected Context ctx;
//    /**
//     * 设置为false并且title为空是，才可以隐藏标题
//     */
//    private boolean isShowTitle = true;
//    private boolean cancelable = true;
//
//    private DialogInterface.OnClickListener listener;//所有按钮的监听
//    private DialogInterface.OnClickListener positiveListener;
//    private DialogInterface.OnClickListener negativeListener;
//    protected DialogInterface.OnClickListener neutralListener;
//
//    private DialogInterface.OnShowListener showListener;
//
//    private AlertDialog dialog;//实际show出来的对象
//
//    private int animRes;
//
//    private final int defaultSize = 18;//title、Button的默认大小
//    private final int defaultTitleColor = App.app.getResources().getColor(R.color.dlg_title);
//    private final int defaultPositiveColor = App.app.getResources().getColor(R.color.dlg_btn_positive);
//    private final int defaultNegativeColor = App.app.getResources().getColor(R.color.dlg_btn_negative);
//
//    private FrameLayout containerView;
//    private FrameLayout rootView;
//
//    public AlertDialog getDialog() {
//        return dialog;
//    }
//
//    public DialogMD(Context ctx) {
//        this.ctx = ctx;
//    }
//
//
//
//    /**
//     * 单确定按钮
//     */
//    public DialogMD(Context ctx, CharSequence message) {
//        this.message = message;
//        this.ctx = ctx;
//    }
//
//    public DialogMD(Context ctx, @StringRes int messageRes) {
//        this.message = ctx.getString(messageRes);
//        this.ctx = ctx;
//    }
//
//    public DialogMD setShowTitle(boolean showTitle) {
//        isShowTitle = showTitle;
//        return this;
//    }
//
//    public DialogMD show() {
//        if (dialog == null) {
//            create();
//        }
//
//        try {
//            dialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return this;
//        }
//
//        Window window = dialog.getWindow();
//        if (window != null) {
//            window.setLayout(-1, -2);
//            if (animRes != 0) {
//                window.setWindowAnimations(animRes);
//            }
//        }
//        for (int i = -3; i < 0; i++) {
//            Button button = dialog.getButton(i);
//            if (button != null) {
////                button.setAllCaps(false);
//                button.setTypeface(null, Typeface.NORMAL);
//                Logs.i(TAG, "show:" + i + ":::::" + button.getPaddingLeft() + ",," + button.getPaddingTop() + ",," + button.getPaddingRight() + ",," + button.getPaddingBottom());
//                if (button.getPaddingLeft() == 0) {
//                    button.setPadding(dp2px(5), 0, dp2px(5), 0);
//                }
//            }
//        }
//        return this;
//    }
//
//    public DialogMD setAnimationsRes(@StyleRes int res) {
//        this.animRes = res;
//        return this;
//    }
//
//    boolean isFlyme6() {
//        return Build.DISPLAY.contains("Flyme 6");
//    }
//
//    private static final String TAG = "DialogMD";
//
//    private void createRootContent(boolean isWrapScrolled) {
//        containerView = new FrameLayout(ctx);
//        containerView.setPadding(dp2px(25), dp2px(10), dp2px(25), dp2px(10));
//        containerView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
//        rootView = containerView;
//        if (isWrapScrolled) {
//            ScrollView scrollView = new ScrollView(ctx);
//            scrollView.addView(containerView, new FrameLayout.LayoutParams(-1, -2));
//            rootView = scrollView;
//        }
//
//
////        rootView = new FrameLayout(ctx);
////        rootView.setPadding(dp2px(25), dp2px(10), dp2px(25), dp2px(10));
////        rootView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
////        containerView = rootView;
////        if (isWrapScrolled) {
////            ScrollView scrollView = new ScrollView(ctx);
//////            scrollView.addView(containerView, new FrameLayout.LayoutParams(-1, -2));
////            rootView.addView(scrollView, new FrameLayout.LayoutParams(-1, -1));
////            containerView = scrollView;
////        }
//    }
//
//    public DialogMD create() {
//        if (isShowTitle && TextUtils.isEmpty(title)) {
//            setTitle("提示：");
//        }
//        if (TextUtils.isEmpty(btnPositiveText)) setBtnPositive("确定");
//
//        if (listener != null) {
//            positiveListener = listener;
//            negativeListener = listener;
//            neutralListener = listener;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.SelfMDAlertStyle);
//
//        if (titleView == null) {
//            builder.setTitle(title);
//        } else {
//            builder.setCustomTitle(titleView);
//            ((TextView) titleView.findViewById(R.id.title)).setText(title);
//        }
//
//        if (rootView == null) {
//            builder.setMessage(message);
//        } else {
//
//            builder.setView(rootView);
//        }
//
//        dialog = builder
//                .setPositiveButton(btnPositiveText, positiveListener)
//                .setNegativeButton(btnNegativeText, negativeListener)
//                .setNeutralButton(btnNeutralText, neutralListener)
//                .create();
//
//        if (showListener != null) {
//            dialog.setOnShowListener(showListener);
//        }
//
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(cancelable);
//        return this;
//    }
//
//    /**
//     * @param whichButton -1 Positive -2 negative -3 neutral
//     * @return
//     */
//    public Button getButton(int whichButton) {
//        if (dialog != null) {
//            return dialog.getButton(whichButton);
//        }
//        return null;
//    }
//
//    public DialogMD setTitleView(View view) {
//        this.titleView = view;
//        return this;
//    }
//
//    public DialogMD setTitleView(@LayoutRes int res) {
//        this.titleView = LayoutInflater.from(ctx).inflate(res, null);
//        return this;
//    }
//
////    public DialogMD setDefaultCustomTitle() {
////        return setTitleView(R.layout.dlg_default_custom_title);
////    }
//
//    public DialogMD setDefaultBtnNegative() {
//        return setBtnNegative(R.string.btn_cancel);
//    }
//
//    public void dismiss() {
//        if (dialog != null) {
//            try {
//                dialog.dismiss();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e(TAG, "dismiss: the dialog is null when dismiss in the DialogMD ");
//        }
//    }
//
//    public View getCustomView() {
//        return customView;
//    }
//
//    public DialogMD setCustomView(@LayoutRes int res) {
//        return setCustomView(res, false);
//    }
//
//    public DialogMD setCustomView(@LayoutRes int res, boolean isWrapScrolled) {
//        View view = LayoutInflater.from(ctx).inflate(res, null);
//        setCustomView(view, isWrapScrolled);
//        return this;
//    }
//
//    public DialogMD setCustomView(View view, boolean isWrapScrolled) {
//        this.customView = view;
//        createRootContent(isWrapScrolled);
//        containerView.addView(view);
//        return this;
//    }
//
//    /**
//     * 自定义 Root Frame 的样式
//     *
//     * @param view
//     * @return
//     */
//    public DialogMD setCustomFrame(FrameLayout view) {
//        this.customView = view;
//        this.rootView = view;
//        return this;
//    }
//
//    public DialogMD setShowListener(DialogInterface.OnShowListener showListener) {
//        this.showListener = showListener;
//        return this;
//    }
//
//    public DialogMD setMessage(CharSequence message) {
//        this.message = message;
//        return this;
//    }
//
//    /**
//     * @param message msg
//     * @param color   color 资源
//     * @param size    字体大小  dp
//     */
//    public DialogMD setMessage(CharSequence message, @ColorRes int color, int size, boolean isBold) {
//        CharSequence tempMsg = getColorSizeString(message, ctx.getResources().getColor(color), size);
//        if (isBold) {
//            this.message = getBoldString(tempMsg);
//        } else {
//            this.message = tempMsg;
//        }
//        return this;
//    }
//
//    public DialogMD setListener(DialogInterface.OnClickListener listener) {
//        this.listener = listener;
//        return this;
//    }
//
//    public DialogMD setPositiveListener(DialogInterface.OnClickListener positiveListener) {
//        this.positiveListener = positiveListener;
//        return this;
//    }
//
//    public DialogMD setNegativeListener(DialogInterface.OnClickListener negativeListener) {
//        this.negativeListener = negativeListener;
//        return this;
//    }
//
//    public DialogMD setNeutralListener(DialogInterface.OnClickListener neutralListener) {
//        this.neutralListener = neutralListener;
//        return this;
//    }
//
//    public DialogMD setCancelable(boolean cancelable) {
//        this.cancelable = cancelable;
//        return this;
//    }
//
//    public DialogMD setTitle(CharSequence title) {
//        this.title = getColorSizeString(title, defaultTitleColor, defaultSize);
//        return this;
//    }
//
//    public DialogMD setTitle(CharSequence title, @ColorRes int color) {
//        this.title = getColorSizeString(title, ctx.getResources().getColor(color), defaultSize);
//        return this;
//    }
//
//    public DialogMD setTitle(@StringRes int res) {
//        return setTitle(ctx.getString(res));
//    }
//
//
//    public DialogMD setBtnPositive(CharSequence text, @ColorRes int color) {
//        this.btnPositiveText = getColorSizeString(text, ctx.getResources().getColor(color), defaultSize);
//        return this;
//    }
//
//    public DialogMD setBtnPositive(@StringRes int text, @ColorRes int color) {
//        return setBtnPositive(ctx.getString(text), color);
//    }
//
//    public DialogMD setBtnNegative(CharSequence text, @ColorRes int color) {
//        this.btnNegativeText = getColorSizeString(text, ctx.getResources().getColor(color), defaultSize);
//        return this;
//    }
//
//    public DialogMD setBtnNegative(@StringRes int text, @ColorRes int color) {
//        return setBtnNegative(ctx.getString(text), color);
//    }
//
//    public DialogMD setBtnNeutral(CharSequence text, @ColorRes int color) {
//        this.btnNeutralText = getColorSizeString(text, ctx.getResources().getColor(color), defaultSize);
//        return this;
//    }
//
//    public DialogMD setBtnNeutral(@StringRes int text, @ColorRes int color) {
//        return setBtnNeutral(ctx.getString(text), color);
//    }
//
//    public DialogMD setBtnPositive(CharSequence btnPositiveText) {
////        this.btnPositiveText = btnPositiveText;
//        this.btnPositiveText = getColorSizeString(btnPositiveText, defaultPositiveColor, defaultSize);
//        return this;
//    }
//
//    public DialogMD setBtnPositive(@StringRes int text) {
//        return setBtnPositive(ctx.getString(text));
//    }
//
//    public DialogMD setBtnNegative(CharSequence btnNegativeText) {
////        this.btnNegativeText = btnNegativeText;
//        this.btnNegativeText = getColorSizeString(btnNegativeText, defaultNegativeColor, defaultSize);
//        return this;
//    }
//
//    public DialogMD setBtnNegative(@StringRes int text) {
//        return setBtnNegative(ctx.getString(text));
//    }
//
//    public DialogMD setBtnNeutral(CharSequence btnNeutralText) {
////        this.btnNeutralText = btnNeutralText;
//        this.btnNeutralText = getColorSizeString(btnNeutralText, defaultPositiveColor, defaultSize);
//        return this;
//    }
//
//    public DialogMD setBtnNeutral(@StringRes int text) {
//        return setBtnNeutral(ctx.getString(text));
//    }
//
//    protected CharSequence getDefaultStyle(CharSequence text) {
//        return getColorSizeString(text, defaultPositiveColor, defaultSize);
//    }
//
//    private CharSequence getColorSizeString(CharSequence text, @ColorInt int color, int size) {
//        if (TextUtils.isEmpty(text)) return text;
//        SpannableString ss = new SpannableString(text);
//        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new AbsoluteSizeSpan(size, true), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return ss;
//    }
//
//    private CharSequence getColorString(CharSequence text, @ColorInt int color) {
//        if (TextUtils.isEmpty(text)) return text;
//        SpannableString ss = new SpannableString(text);
//        ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return ss;
//    }
//
//    private CharSequence getBoldString(CharSequence text) {
//        if (TextUtils.isEmpty(text)) return text;
//        SpannableString ss = new SpannableString(text);
//        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return ss;
//    }
//
//    private CharSequence getSizeString(CharSequence text, int size) {
//        if (TextUtils.isEmpty(text)) return text;
//        if (size <= 0) return text;
//        SpannableString ss = new SpannableString(text);
//        ss.setSpan(new AbsoluteSizeSpan(size, true), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return ss;
//    }
//
//}
