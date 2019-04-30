package com.xia.rxbus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

import com.xia.flyrxbus.RxBus
import com.xia.flyrxbus.RxBusManager
import com.xia.flyrxbus.RxBusMessage

import androidx.appcompat.app.AppCompatActivity

class StickyTestActivity : AppCompatActivity() {

    private var mTvSticky: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticky_test)

        mTvSticky = findViewById(R.id.tv_sticky)

        RxBus.subscribeSticky(this, object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("sticky without $s")
            }
        })

        RxBus.subscribeSticky(this, "my tag", object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("sticky with $s")
            }
        })

        RxBusManager.subscribeStickyWithTags(this, object : RxBus.Callback<RxBusMessage>() {
            override fun onEvent(tag: String, rxBusMessage: RxBusMessage) {
                Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                if (rxBusMessage.mObj is TestEvent) {
                    val testEvent = rxBusMessage.mObj as TestEvent
                    Log.e("weixi", "onEvent: " + testEvent.mString)
                }
            }
        }, "myTag4", "myTag5", "myTag6")
    }

    fun postWithoutTag(view: View) {
        Config.restoreMsg()
        RxBus.post("tag")
    }

    fun postWithTag(view: View) {
        Config.restoreMsg()
        RxBus.post("tag", "my tag")
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unregister(this)
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, StickyTestActivity::class.java)
            context.startActivity(starter)
        }
    }
}
