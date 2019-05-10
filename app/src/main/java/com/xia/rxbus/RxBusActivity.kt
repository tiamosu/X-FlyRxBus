package com.xia.rxbus

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.xia.flyrxbus.RxBus
import com.xia.flyrxbus.RxBusManager
import com.xia.flyrxbus.RxBusMessage

class RxBusActivity : AppCompatActivity() {

    private var mTvSticky: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvSticky = findViewById(R.id.tv_sticky)

        RxBusManager.subscribe(this, object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("without $s")
                throw NullPointerException("")
            }
        })

        RxBusManager.subscribe(this, "my tag", object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("with $s")
            }
        })

        RxBusManager.subscribeWithTags(this, object : RxBus.Callback<RxBusMessage>() {
            override fun onEvent(tag: String, rxBusMessage: RxBusMessage) {
                if ("myTag1" == tag) {
                    Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                }
                if ("myTag2" == tag) {
                    Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                }
                if ("myTag3" == tag) {
                    Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                    if (rxBusMessage.mObj is TestEvent) {
                        val testEvent = rxBusMessage.mObj as TestEvent
                        Log.e("weixi", "tag:" + tag + "    onEvent: " + testEvent.mString)
                    }
                }
            }
        }, "myTag1", "myTag2", "myTag3")
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBusManager.unregister(this)
    }

    fun postWithoutTag(view: View) {
        Config.restoreMsg()
        RxBusManager.post("tag")
    }

    fun postWithTag(view: View) {
        Config.restoreMsg()
        RxBusManager.post("with tag", "my tag")

        RxBusManager.post(RxBusMessage("1"), "myTag1")
        RxBusManager.post(RxBusMessage(0.001), "myTag2")
        RxBusManager.post(RxBusMessage(TestEvent("hello")), "myTag3")
    }

    fun postStickyWithoutTag(view: View) {
        Config.restoreMsg()
        RxBusManager.postSticky("tag")
        StickyTestActivity.start(this)
    }

    fun postStickyWithTag(view: View) {
        Config.restoreMsg()
        RxBusManager.postSticky("tag", "my tag")

        RxBusManager.postSticky(RxBusMessage("1"), "myTag4")
        RxBusManager.postSticky(RxBusMessage(0.001), "myTag5")
        RxBusManager.postSticky(RxBusMessage(TestEvent("hello")), "myTag6")
        StickyTestActivity.start(this)
    }

    fun useManager(view: View) {
        RxBusManagerActivity.start(this)
        finish()
    }
}
