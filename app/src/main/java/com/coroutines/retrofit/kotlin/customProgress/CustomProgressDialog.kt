package com.secure.omega.app.view.customViews.customProgress

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import com.coroutines.retrofit.kotlin.R
import com.coroutines.retrofit.kotlin.customProgress.CircleImageView
import com.coroutines.retrofit.kotlin.customProgress.MaterialProgressDrawable


class CustomProgressDialog : AlertDialog {
    private var mContext: Context? = null
    private var mCircleView: CircleImageView? = null
    private var mProgress: MaterialProgressDrawable? = null
    private val mMediumAnimationDuration = 500
    private var mHasStarted: Boolean = false
    private var mScaleAnimation: Animation? = null
    private val mListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}

        override fun onAnimationRepeat(animation: Animation?) {}

        @SuppressLint("NewApi")
        override fun onAnimationEnd(animation: Animation?) {
            mProgress!!.alpha = MAX_ALPHA
            mProgress!!.start()
        }
    }

    private val isAlphaUsedForScale: Boolean
        get() = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_progress_dialog_layout)
        val mWindow = window
        val mView = mWindow!!.decorView
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mWindow.setGravity(Gravity.CENTER)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        mView.setBackgroundResource(android.R.color.transparent)
        val parent = mView.findViewById<View>(R.id.sr_container) as ViewGroup
        createProgressView(parent)
    }

    private fun createProgressView(parent: ViewGroup?) {
        mCircleView = CircleImageView(context, CIRCLE_BG_LIGHT)
        mProgress = MaterialProgressDrawable(context, parent!!)
        mProgress!!.setBackgroundColor(CIRCLE_BG_LIGHT)
        mCircleView!!.setImageDrawable(mProgress)
        parent!!.addView(mCircleView)
        setContentView(parent)
    }

    public override fun onStart() {
        super.onStart()
        startScaleUpAnimation(mListener)
        mHasStarted = true
    }

    override fun onStop() {
        super.onStop()
        mProgress!!.stop()
        mHasStarted = false
    }

    @SuppressLint("NewApi")
    private fun startScaleUpAnimation(listener: Animation.AnimationListener?) {
        mCircleView!!.visibility = View.VISIBLE
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // Pre API 11, alpha is used in place of scale up to show the
            // progress circle appearing.
            // Don't adjust the alpha during appearance otherwise.
            mProgress!!.alpha = MAX_ALPHA
        }
        mScaleAnimation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                setAnimationProgress(interpolatedTime)
            }
        }
        mScaleAnimation!!.duration = mMediumAnimationDuration.toLong()
        if (listener != null) {
            mCircleView!!.setAnimationListener(listener)
        }
        mCircleView!!.clearAnimation()
        mCircleView!!.startAnimation(mScaleAnimation)
    }

    internal fun setAnimationProgress(progress: Float) {
        if (isAlphaUsedForScale) {
            setColorViewAlpha((progress * MAX_ALPHA).toInt())
        } else {
            ViewCompat.setScaleX(mCircleView!!, progress)
            ViewCompat.setScaleY(mCircleView!!, progress)
        }
    }

    @SuppressLint("NewApi")
    private fun setColorViewAlpha(targetAlpha: Int) {
        mCircleView!!.background.alpha = targetAlpha
        mProgress!!.alpha = targetAlpha
    }

    companion object {

        // Default background for the progress spinner
        private val CIRCLE_BG_LIGHT = -0x50506
        private val MAX_ALPHA = 255

        var dialog : CustomProgressDialog? = null


        public fun show(context: Context?, cancelable: Boolean?): CustomProgressDialog {

            try {
                if(dialog != null){
                    if(dialog!!.isShowing)
                        dialog!!.dismiss()
                }

                dialog = CustomProgressDialog(context!! , R.style.pdStyle)
                dialog!!.setCanceledOnTouchOutside(cancelable!!)
                dialog!!.setCancelable(cancelable!!)
                dialog!!.show()

            } catch (e: Exception) {

                dialog = CustomProgressDialog(context!! , R.style.pdStyle)
                dialog!!.setCanceledOnTouchOutside(cancelable!!)
                dialog!!.setCancelable(cancelable!!)
                dialog!!.show()
            }
            return dialog as CustomProgressDialog
        }

    }



    constructor(context: Context) : super(context) {
        init(context)
    }


    constructor(context: Context, theme: Int) : super(context, theme) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
    }

    private var runnable: Runnable? = null
    private val handler = Handler()
    fun timerDelayRemoveDialog(time: Long) {

        runnable = Runnable {
            if (isShowing)
                cancel()
        }
        handler.postDelayed(runnable, time)
    }

    fun removeExistingNotificationTimeoutThread() {
        handler?.removeCallbacks(runnable)
    }
}