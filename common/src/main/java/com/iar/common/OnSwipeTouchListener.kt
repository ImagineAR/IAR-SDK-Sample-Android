package com.iar.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    private val gestureDetector: GestureDetector
    private val SWIPE_DISTANCE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    open fun onSwipeRight() {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(
                    velocityX
                ) > SWIPE_VELOCITY_THRESHOLD
            ) {

                if (distanceX > 0) onSwipeRight()
                return true
            }
            return false
        }

    }

}