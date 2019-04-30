package com.xia.flyrxbus

import androidx.annotation.NonNull
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

@Suppress("unused")
object RxBus {

    private val BUS: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()
    private val ON_ERROR = Consumer<Throwable> { throwable -> Utils.logE(throwable.toString()) }

    @JvmStatic
    fun post(@NonNull event: Any) {
        post(event, "", false)
    }

    @JvmStatic
    fun post(@NonNull event: Any, @NonNull tag: String) {
        post(event, tag, false)
    }

    @JvmStatic
    fun postSticky(@NonNull event: Any) {
        post(event, "", true)
    }

    @JvmStatic
    fun postSticky(@NonNull event: Any, @NonNull tag: String) {
        post(event, tag, true)
    }

    private fun post(@NonNull event: Any,
                     @NonNull tag: String,
                     isSticky: Boolean) {
        val msgEvent = TagMessage(event, tag)
        if (isSticky) {
            CacheUtils.addStickyEvent(event, tag)
        }
        BUS.onNext(msgEvent)
    }

    @JvmStatic
    @JvmOverloads
    fun removeSticky(@NonNull event: Any,
                     @NonNull tag: String = "") {
        CacheUtils.removeStickyEvent(event, tag)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      @NonNull callback: Callback<T>) {
        subscribe(subscriber, "", false, null, callback)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      @NonNull tag: String,
                      @NonNull callback: Callback<T>) {
        subscribe(subscriber, tag, false, null, callback)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      scheduler: Scheduler?,
                      @NonNull callback: Callback<T>) {
        subscribe(subscriber, "", false, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribe(@NonNull subscriber: Any,
                      @NonNull tag: String,
                      scheduler: Scheduler?,
                      @NonNull callback: Callback<T>) {
        subscribe(subscriber, tag, false, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            @NonNull callback: Callback<T>) {
        subscribe(subscriber, "", true, null, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            @NonNull tag: String,
                            @NonNull callback: Callback<T>) {
        subscribe(subscriber, tag, true, null, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            scheduler: Scheduler?,
                            @NonNull callback: Callback<T>) {
        subscribe(subscriber, "", true, scheduler, callback)
    }

    @JvmStatic
    fun <T> subscribeSticky(@NonNull subscriber: Any,
                            @NonNull tag: String,
                            scheduler: Scheduler?,
                            @NonNull callback: Callback<T>) {
        subscribe(subscriber, tag, true, scheduler, callback)
    }

    @JvmStatic
    private fun <T> subscribe(@NonNull subscriber: Any,
                              @NonNull tag: String,
                              isSticky: Boolean,
                              scheduler: Scheduler?,
                              @NonNull callback: Callback<T>) {

        val typeClass = Utils.getTypeClassFromParadigm(callback) ?: return
        val onNext = Consumer<T> { t -> callback.onEvent(tag, t) }

        if (isSticky) {
            val stickyEvent = CacheUtils.findStickyEvent(typeClass, tag)
            if (stickyEvent != null) {
                var stickyFlowable = Flowable.create(FlowableOnSubscribe<T> { emitter ->
                    emitter.onNext(typeClass.cast(stickyEvent.mEvent)!!)
                }, BackpressureStrategy.LATEST)
                if (scheduler != null) {
                    stickyFlowable = stickyFlowable.observeOn(scheduler)
                }
                val stickyDisposable = FlowableUtils.subscribe(stickyFlowable, onNext, ON_ERROR)
                CacheUtils.addDisposable(subscriber, stickyDisposable)
            } else {
                Utils.logW("sticky event is empty.")
            }
        }
        val disposable = FlowableUtils.subscribe(
                toFlowable(typeClass, tag, scheduler), onNext, ON_ERROR
        )
        CacheUtils.addDisposable(subscriber, disposable)
    }

    private fun <T> toFlowable(@NonNull eventType: Class<T>?,
                               @NonNull tag: String,
                               scheduler: Scheduler?): Flowable<T> {
        val flowable = BUS.ofType(TagMessage::class.java)
                .filter { tagMessage -> tagMessage.isSameType(eventType!!, tag) }
                .map { tagMessage -> tagMessage.mEvent }
                .cast(eventType!!)
        return if (scheduler != null) {
            flowable.observeOn(scheduler)
        } else flowable
    }

    @JvmStatic
    fun unregister(@NonNull subscriber: Any) {
        CacheUtils.removeDisposables(subscriber)
    }

    abstract class Callback<T> {
        abstract fun onEvent(tag: String, t: T)
    }
}