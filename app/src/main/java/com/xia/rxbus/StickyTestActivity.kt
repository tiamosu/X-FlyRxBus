package com.xia.rxbus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.xia.flyrxbus.RxBus
import com.xia.flyrxbus.RxBusManager
import com.xia.flyrxbus.RxBusMessage
import kotlinx.android.synthetic.main.activity_sticky_test.*

class StickyTestActivity : AppCompatActivity() {

    private var mTvSticky: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticky_test)

        mTvSticky = findViewById(R.id.tv_sticky)

        RxBusManager.subscribeSticky(this, object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("sticky without $s")
            }
        })

        RxBusManager.subscribeSticky(this, "my tag", object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                mTvSticky!!.text = Config.appendMsg("sticky with $s")
            }
        })

        RxBusManager.subscribeStickyWithTags(this, object : RxBus.Callback<RxBusMessage>() {
            override fun onEvent(tag: String, rxBusMessage: RxBusMessage) {
                if ("myTag4" == tag) {
                    Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                }
                if ("myTag5" == tag) {
                    Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                }
                if ("myTag6" == tag) {
                    Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj)
                    if (rxBusMessage.mObj is TestEvent) {
                        val testEvent = rxBusMessage.mObj as TestEvent
                        Log.e("weixi", "tag:" + tag + "    onEvent: " + testEvent.mString)
                    }
                }
            }
        }, "myTag4", "myTag5", "myTag6")

        btn_post_without_tag.setOnClickListener {
            Config.restoreMsg()
            RxBusManager.post("tag")
        }

        btn_post_with_tag.setOnClickListener {
            Config.restoreMsg()
            RxBusManager.post("tag", "my tag")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBusManager.unregister(this)
    }

    companion object {

        fun start(context: Context) {
            val starter = Intent(context, StickyTestActivity::class.java)
            context.startActivity(starter)
        }
    }
}
