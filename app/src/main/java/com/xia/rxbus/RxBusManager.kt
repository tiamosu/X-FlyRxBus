package com.xia.rxbus

import com.xia.flyrxbus.RxBus

object RxBusManager {

    private const val MY_TAG = "MY_TAG"

    fun subscribeRxBusManagerActivity(activity: RxBusManagerActivity) {
        RxBus.subscribe(activity, object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                activity.updateText("without $s")
            }
        })

        RxBus.subscribe(activity, MY_TAG, object : RxBus.Callback<String>() {
            override fun onEvent(tag: String, s: String) {
                activity.updateText("with $s")
            }
        })
    }

    fun postToRxBusManagerActivity(event: String) {
        RxBus.post(event)
    }

    fun postWithMyTagToRxBusManagerActivity(event: String) {
        RxBus.post(event, MY_TAG)
    }

    fun postStickyToRxBusManagerActivity(event: String) {
        RxBus.postSticky(event)
    }

    fun postStickyWithMyTagToRxBusManagerActivity(event: String) {
        RxBus.postSticky(event, MY_TAG)
    }

    fun unregisterRxBusManagerActivity(activity: RxBusManagerActivity) {
        RxBus.unregister(activity)
    }
}
