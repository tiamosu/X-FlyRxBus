package com.xia.rxbus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xia.flyrxbus.RxBus;
import com.xia.flyrxbus.RxBusManager;
import com.xia.flyrxbus.RxBusMessage;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;

public class StickyTestActivity extends AppCompatActivity {

    private TextView tvSticky;

    public static void start(Context context) {
        Intent starter = new Intent(context, StickyTestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_test);

        tvSticky = findViewById(R.id.tv_sticky);

        RxBus.subscribeSticky(this, new RxBus.Callback<String>() {
            @Override
            public void onEvent(@NotNull String tag, String s) {
                tvSticky.setText(Config.appendMsg("sticky without " + s));
            }
        });

        RxBus.subscribeSticky(this, "my tag", new RxBus.Callback<String>() {
            @Override
            public void onEvent(@NotNull String tag, String s) {
                tvSticky.setText(Config.appendMsg("sticky with " + s));
            }
        });

        RxBusManager.subscribeStickyWithTags(this, new RxBus.Callback<RxBusMessage>() {
            @Override
            public void onEvent(@NotNull String tag, RxBusMessage rxBusMessage) {
                Log.e("weixi", "tag:" + tag + "    obj:" + rxBusMessage.mObj);
                if (rxBusMessage.mObj instanceof TestEvent) {
                    final TestEvent testEvent = (TestEvent) rxBusMessage.mObj;
                    Log.e("weixi", "onEvent: " + testEvent.mString);
                }
            }
        }, "myTag4", "myTag5", "myTag6");
    }

    public void postWithoutTag(View view) {
        Config.restoreMsg();
        RxBus.post("tag");
    }

    public void postWithTag(View view) {
        Config.restoreMsg();
        RxBus.post("tag", "my tag");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.unregister(this);
    }
}
