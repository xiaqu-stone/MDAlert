package com.stone.mddemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.mdlib.MDAlert
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        MDAlert.defaultPositiveColor = Color.parseColor("#FFFF00")
        btnSingleAlertKt.setOnClickListener { _ -> MDAlert(ctx, "Kotlin的调用测试").setBtnPositive { toast("Click Positive") }.show() }
        btnDoubleAlertKt.setOnClickListener { _ -> MDAlert(ctx, R.style.MyAlertStyle).setMessage("Kotlin的双按钮").setBtnNegative { toast("Click Negative") }.setBtnPositive { toast("Click Positive") }.setCancelable(false).setCancel().show() }

        btnListener.setOnClickListener { _ ->
            MDAlert(ctx, "Listener Test").setCancel().setBtnNeutral("中间", color = Color.parseColor("#00FFFF"))
                    .setBtnPositive { toast("确定") }
                    .setListener { _, which ->
                        when (which) {
                            -1 -> toast("Click Positive")
                            -2 -> toast("Click Negative")
                            -3 -> toast("Click Neutral")
                        }
                    }.show()
        }

        btnSingleAlert.setOnClickListener { MDAlertTest.showSingle(ctx, "Java中测试") }
        btnDoubleAlert.setOnClickListener { MDAlertTest.showSelect(ctx, "Java 中的双按钮") }
    }
}
