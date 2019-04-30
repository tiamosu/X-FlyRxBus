package com.xia.rxbus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

class RxBusManagerActivity : AppCompatActivity() {

    private var mTvSticky: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn_use_manager).visibility = View.GONE

        mTvSticky = findViewById(R.id.tv_sticky)

        RxBusManager.subscribeRxBusManagerActivity(this)
    }

    fun updateText(s: String) {
        mTvSticky!!.text = Config.appendMsg(s)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBusManager.unregisterRxBusManagerActivity(this)
    }

    fun postWithoutTag(view: View) {
        Config.restoreMsg()
        RxBusManager.postToRxBusManagerActivity("tag")
    }

    fun postWithTag(view: View) {
        Config.restoreMsg()
        RxBusManager.postWithMyTagToRxBusManagerActivity("tag")
    }

    fun postStickyWithoutTag(view: View) {
        Config.restoreMsg()
        RxBusManager.postStickyToRxBusManagerActivity("tag")
        StickyTestActivity.start(this)
    }

    fun postStickyWithTag(view: View) {
        Config.restoreMsg()
        RxBusManager.postStickyWithMyTagToRxBusManagerActivity("tag")
        StickyTestActivity.start(this)
    }

    fun useManager(view: View) {}

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, RxBusManagerActivity::class.java)
            context.startActivity(starter)
        }
    }
}
