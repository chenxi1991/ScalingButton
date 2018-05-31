package com.chenxi1991.scalingbutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mButtons = mutableListOf<ScalingButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtons()
    }

    override fun onClick(v: View?) {
        resetState(v as ScalingButton)
    }

    private fun initButtons() {
        mButtons.add(btn1)
        mButtons.add(btn2)
        mButtons.forEach({
            it.setOnClickListener(this)
        })
    }

    private fun resetState(view: ScalingButton) {
        mButtons.forEach({
            if (it.id != view.id) {
                if (it.getState())
                    it.toggleState()
            }
        })
        if (!view.getState())
            view.toggleState()
    }
}
