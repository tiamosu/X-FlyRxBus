package com.xia.rxbus;

import com.xia.flyrxbus.RxBus;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
public class RxBusManager {

    private static final String MY_TAG = "MY_TAG";

    public static void subscribeRxBusManagerActivity(final RxBusManagerActivity activity) {
        RxBus.subscribe(activity, new RxBus.Callback<String>() {
            @Override
            public void onEvent(@NotNull String tag, String s) {
                activity.updateText("without " + s);
            }
        });

        RxBus.subscribe(activity, MY_TAG, new RxBus.Callback<String>() {
            @Override
            public void onEvent(@NotNull String tag, String s) {
                activity.updateText("with " + s);
            }
        });
    }

    public static void postToRxBusManagerActivity(final String event) {
        RxBus.post(event);
    }

    public static void postWithMyTagToRxBusManagerActivity(final String event) {
        RxBus.post(event, MY_TAG);
    }

    public static void postStickyToRxBusManagerActivity(final String event) {
        RxBus.postSticky(event);
    }

    public static void postStickyWithMyTagToRxBusManagerActivity(final String event) {
        RxBus.postSticky(event, MY_TAG);
    }

    public static void unregisterRxBusManagerActivity(final RxBusManagerActivity activity) {
        RxBus.unregister(activity);
    }
}
