package com.chenxi1991.scalingbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mButtons = mutableListOf<ScalingButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }


    private fun initButtons() {
        mButtons.add(btn1)
        mButtons.add(btn2)

        btn1.setOnClickListener({
            when (btn1.getState()) {
                true -> {
                    when ((btn1 as ScalingButton).getCurrentResourceIds()[0]) {
                        R.mipmap.hardpen_pre -> {
                            btn1.changeImageResources(R.mipmap.eraser_point_pre, R.mipmap.eraser_point_nor)
                        }
                        R.mipmap.eraser_point_pre -> {
                            btn1.changeImageResources(R.mipmap.hardpen_pre, R.mipmap.hardpen_nor)
                        }
                    }
                }
                false -> {
                    btn1.toggleState()
//                    btn2.toggleState()
                }
            }
        })

        btn2.setOnClickListener({
            btn1.toggleState()
//            btn2.toggleState()
        })
    }
}
