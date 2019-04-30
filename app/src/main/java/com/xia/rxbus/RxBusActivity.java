package com.xia.rxbus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xia.flyrxbus.RxBus;
import com.xia.flyrxbus.RxBusManager;
import com.xia.flyrxbus.RxBusMessage;

import androidx.appcompat.app.AppCompatActivity;

public class RxBusActivity extends AppCompatActivity {

    private TextView tvSticky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSticky = findViewById(R.id.tv_sticky);

        RxBus.getDefault().subscribe(this, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String tag, String s) {
                tvSticky.setText(Config.appendMsg("without " + s));
                throw new NullPointerException("");
            }
        });

        RxBus.getDefault().subscribe(this, "my tag", new RxBus.Callback<String>() {
            @Override
            public void onEvent(String tag, String s) {
                tvSticky.setText(Config.appendMsg("with " + s));
            }
        });

        RxBusManager.subscribeWithTags(this, new RxBus.Callback<RxBusMessage>() {
            @Override
            public void onEvent(String tag, RxBusMessage rxBusMessage) {
                Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj);
                if (rxBusMessage.mObj instanceof TestEvent) {
                    final TestEvent testEvent = (TestEvent) rxBusMessage.mObj;
                    Log.e("weixi", "onEvent: " + testEvent.mString);
                }
            }
        }, "myTag1", "myTag2", "myTag3");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(this);
    }

    public void postWithoutTag(View view) {
        Config.restoreMsg();
        RxBus.getDefault().post("tag");
    }

    public void postWithTag(View view) {
        Config.restoreMsg();
        RxBus.getDefault().post("with tag", "my tag");

        RxBus.getDefault().post(new RxBusMessage("1"), "myTag1");
        RxBus.getDefault().post(new RxBusMessage(0.001), "myTag2");
        RxBus.getDefault().post(new RxBusMessage(new TestEvent("hello")), "myTag3");
    }

    public void postStickyWithoutTag(View view) {
        Config.restoreMsg();
        RxBus.getDefault().postSticky("tag");
        StickyTestActivity.start(this);
    }

    public void postStickyWithTag(View view) {
        Config.restoreMsg();
        RxBus.getDefault().postSticky("tag", "my tag");

        RxBus.getDefault().postSticky(new RxBusMessage("1"), "myTag4");
        RxBus.getDefault().postSticky(new RxBusMessage(0.001), "myTag5");
        RxBus.getDefault().postSticky(new RxBusMessage(new TestEvent("hello")), "myTag6");
        StickyTestActivity.start(this);
    }

    public void useManager(View view) {
        RxBusManagerActivity.start(this);
        finish();
    }
}
