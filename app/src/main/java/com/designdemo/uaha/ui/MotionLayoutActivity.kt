package com.designdemo.uaha.ui

import android.os.Bundle
import android.app.Activity
import android.util.Log
import android.widget.Button
import androidx.constraintlayout.motion.widget.MotionLayout
import com.support.android.designlibdemo.R

import kotlinx.android.synthetic.main.activity_motion_layout.*

class MotionLayoutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout)

        val infoButton = launch_button
        infoButton.setOnClickListener{v -> launchMotionScene()}
    }

    private fun launchMotionScene() {
        val motionLayout = motionlayout_demo
        motionLayout.transitionToEnd()
    }

}
