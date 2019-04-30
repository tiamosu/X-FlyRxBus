package com.xia.rxbus

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

import com.xia.flyrxbus.RxBus
import com.xia.flyrxbus.RxBusManager
import com.xia.flyrxbus.RxBusMessage

import androidx.appcompat.app.AppCompatActivity

class RxBusActivity : AppCompatActivity() {

    private var mTvSticky: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvSticky = findViewById(R.id.tv_sticky)

        RxBus.subscribe(this, object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("without $s")
                throw NullPointerException("")
            }
        })

        RxBus.subscribe(this, "my tag", object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("with $s")
            }
        })

        RxBusManager.subscribeWithTags(this, object : RxBus.Callback<RxBusMessage>() {
            override fun onEvent(tag: String, rxBusMessage: RxBusMessage) {
                Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                if (rxBusMessage.mObj is TestEvent) {
                    val testEvent = rxBusMessage.mObj as TestEvent
                    Log.e("weixi", "onEvent: " + testEvent.mString)
                }
            }
        }, "myTag1", "myTag2", "myTag3")
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unregister(this)
    }

    fun postWithoutTag(view: View) {
        Config.restoreMsg()
        RxBus.post("tag")
    }

    fun postWithTag(view: View) {
        Config.restoreMsg()
        RxBus.post("with tag", "my tag")

        RxBus.post(RxBusMessage("1"), "myTag1")
        RxBus.post(RxBusMessage(0.001), "myTag2")
        RxBus.post(RxBusMessage(TestEvent("hello")), "myTag3")
    }

    fun postStickyWithoutTag(view: View) {
        Config.restoreMsg()
        RxBus.postSticky("tag")
        StickyTestActivity.start(this)
    }

    fun postStickyWithTag(view: View) {
        Config.restoreMsg()
        RxBus.postSticky("tag", "my tag")

        RxBus.postSticky(RxBusMessage("1"), "myTag4")
        RxBus.postSticky(RxBusMessage(0.001), "myTag5")
        RxBus.postSticky(RxBusMessage(TestEvent("hello")), "myTag6")
        StickyTestActivity.start(this)
    }

    fun useManager(view: View) {
        RxBusManagerActivity.start(this)
        finish()
    }
}
