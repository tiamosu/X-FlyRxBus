package com.xia.flyrxbus

import androidx.annotation.NonNull
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.operators.flowable.FlowableInternalHelper
import org.reactivestreams.Subscription

object FlowableUtils {

    @JvmStatic
    fun <T> subscribe(@NonNull flowable: Flowable<T>,
                      @NonNull onNext: Consumer<in T>,
                      @NonNull onError: Consumer<in Throwable>): Disposable {
        return subscribe(flowable,
                onNext, onError,
                Functions.EMPTY_ACTION,
                FlowableInternalHelper.RequestMax.INSTANCE
        )
    }

    @JvmStatic
    private fun <T> subscribe(@NonNull flowable: Flowable<T>,
                              @NonNull onNext: Consumer<in T>,
                              @NonNull onError: Consumer<in Throwable>,
                              @NonNull onComplete: Action,
                              @NonNull onSubscribe: Consumer<in Subscription>): Disposable {

        val ls = MyLambdaSubscriber(onNext, onError, onComplete, onSubscribe)
        flowable.subscribe(ls)
        return ls
    }
}
