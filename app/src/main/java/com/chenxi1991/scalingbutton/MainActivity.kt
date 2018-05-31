package com.chenxi1991.scalingbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mButtons = mutableListOf<ScalingButton>()

    private var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }


    private fun initButtons() {
        mButtons.add(btn1)
        mButtons.add(btn2)

        btn1.setOnClickListener({
            if (btn1.getState()) {
                if (state == 0) {
                    state = 1
                    btn1.setImageResources(R.mipmap.eraser_point_pre, R.mipmap.eraser_point_nor)
                } else {
                    state = 0
                    btn1.setImageResources(R.mipmap.hardpen_pre, R.mipmap.hardpen_nor)
                }
            } else {
                btn1.toggleState()
            }
        })

        btn2.setOnClickListener({
            btn1.toggleState()
        })
    }
}
