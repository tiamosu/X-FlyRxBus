package com.xia.flyrxbus

import androidx.annotation.NonNull
import io.reactivex.Scheduler

@Suppress("unused")
object RxBusManager {

    @JvmStatic
    fun post(@NonNull event: Any) {
        RxBus.post(event)
    }

    @JvmStatic
    fun post(@NonNull event: Any, @NonNull tag: String) {
        RxBus.post(event, tag)
    }

    @JvmStatic
    fun postSticky(@NonNull event: Any) {
        RxBus.postSticky(event)
    }

    @JvmStatic
    fun postSticky(@NonNull event: Any, @NonNull tag: String) {
        RxBus.postSticky(event, tag)
    }

    @JvmStatic
    fun removeSticky(@NonNull event: Any) {
        RxBus.removeSticky(event)
    }

    @JvmStatic
    fun removeSticky(@NonNull event: Any,
                     @NonNull tag: String) {
        RxBus.removeSticky(event, tag)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribe(subscriber, callback)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      scheduler: Scheduler?,
                      @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribe(subscriber, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      @NonNull tag: String,
                      @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribe(subscriber, tag, callback)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      @NonNull tag: String,
                      scheduler: Scheduler?,
                      @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribe(subscriber, tag, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribeSticky(subscriber, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            scheduler: Scheduler?,
                            @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribeSticky(subscriber, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            @NonNull tag: String,
                            @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribeSticky(subscriber, tag, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            @NonNull tag: String,
                            scheduler: Scheduler?,
                            @NonNull callback: RxBus.Callback<T>) {
        RxBus.subscribeSticky(subscriber, tag, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribeWithTags(@NonNull subscriber: Any,
                              @NonNull callback: RxBus.Callback<T>,
                              @NonNull vararg tags: String) {
        subscribeWithTags(subscriber, null, callback, *tags)
    }

    @JvmStatic
    fun <T> subscribeWithTags(@NonNull subscriber: Any,
                              scheduler: Scheduler?,
                              @NonNull callback: RxBus.Callback<T>,
                              @NonNull vararg tags: String) {
        if (tags.isEmpty()) {
            throw RuntimeException("Tags is empty,you should set the tags")
        }
        for (tag in tags) {
            RxBus.subscribe(subscriber, tag, scheduler, callback)
        }
    }

    @JvmStatic
    fun <T> subscribeStickyWithTags(@NonNull subscriber: Any,
                                    @NonNull callback: RxBus.Callback<T>,
                                    @NonNull vararg tags: String) {
        subscribeStickyWithTags(subscriber, null, callback, *tags)
    }

    @JvmStatic
    fun <T> subscribeStickyWithTags(@NonNull subscriber: Any,
                                    scheduler: Scheduler?,
                                    @NonNull callback: RxBus.Callback<T>,
                                    @NonNull vararg tags: String) {
        if (tags.isEmpty()) {
            throw RuntimeException("Tags is empty,you should set the tags")
        }
        for (tag in tags) {
            RxBus.subscribeSticky(subscriber, tag, scheduler, callback)
        }
    }

    @JvmStatic
    fun unregister(@NonNull subscriber: Any) {
        RxBus.unregister(subscriber)
    }
}
