package com.xia.rxbus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class RxBusManagerActivity : AppCompatActivity() {

    private var mTvSticky: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_use_manager.visibility = View.GONE
        mTvSticky = findViewById(R.id.tv_sticky)

        RxBusManager.subscribeRxBusManagerActivity(this)

        btn_post_without_tag.setOnClickListener {
            Config.restoreMsg()
            RxBusManager.postToRxBusManagerActivity("tag")
        }

        btn_post_with_tag.setOnClickListener {
            Config.restoreMsg()
            RxBusManager.postWithMyTagToRxBusManagerActivity("tag")
        }

        btn_post_sticky_without_tag.setOnClickListener {
            Config.restoreMsg()
            RxBusManager.postStickyToRxBusManagerActivity("tag")
            StickyTestActivity.start(this)
        }

        btn_post_sticky_with_tag.setOnClickListener {
            Config.restoreMsg()
            RxBusManager.postStickyWithMyTagToRxBusManagerActivity("tag")
            StickyTestActivity.start(this)
        }
    }

    fun updateText(s: String) {
        mTvSticky!!.text = Config.appendMsg(s)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBusManager.unregisterRxBusManagerActivity(this)
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, RxBusManagerActivity::class.java)
            context.startActivity(starter)
        }
    }
}
