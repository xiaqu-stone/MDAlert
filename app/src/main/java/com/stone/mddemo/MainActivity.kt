package com.stone.mddemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stone.mdlib.MDAlert
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        MDAlert.defaultPositiveColor = Color.parseColor("#FFFF00")
        btnSingleAlertKt.setOnClickListener { MDAlert(ctx, "Kotlin的调用测试").show() }
        btnDoubleAlertKt.setOnClickListener { MDAlert(ctx, R.style.MyAlertStyle).setMessage("Kotlin的双按钮").setCancelable(false).setCancel().show() }
        btnSingleAlert.setOnClickListener { MDAlertTest.showSingle(ctx, "Java中测试") }
        btnDoubleAlert.setOnClickListener { MDAlertTest.showSelect(ctx,"Java 中的双按钮") }
    }
}
