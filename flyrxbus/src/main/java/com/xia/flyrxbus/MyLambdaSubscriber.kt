package com.xia.flyrxbus

import io.reactivex.FlowableSubscriber
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.subscriptions.SubscriptionHelper
import io.reactivex.observers.LambdaConsumerIntrospection
import io.reactivex.plugins.RxJavaPlugins
import org.reactivestreams.Subscription
import java.util.concurrent.atomic.AtomicReference

internal class MyLambdaSubscriber<T>(private val mOnNext: Consumer<in T>,
                                     private val mOnError: Consumer<in Throwable>,
                                     private val mOnComplete: Action,
                                     private val mOnSubscribe: Consumer<in Subscription>)
    : AtomicReference<Subscription>(), FlowableSubscriber<T>, Subscription, Disposable, LambdaConsumerIntrospection {

    override fun onSubscribe(s: Subscription) {
        if (SubscriptionHelper.setOnce(this, s)) {
            try {
                mOnSubscribe.accept(this)
            } catch (ex: Throwable) {
                Exceptions.throwIfFatal(ex)
                s.cancel()
                onError(ex)
            }
        }
    }

    override fun onNext(t: T) {
        if (!isDisposed) {
            try {
                mOnNext.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                onError(e)
            }

        }
    }

    override fun onError(t: Throwable) {
        if (get() !== SubscriptionHelper.CANCELLED) {
            try {
                mOnError.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
            }

        } else {
            RxJavaPlugins.onError(t)
        }
    }

    override fun onComplete() {
        if (get() !== SubscriptionHelper.CANCELLED) {
            lazySet(SubscriptionHelper.CANCELLED)
            try {
                mOnComplete.run()
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(e)
            }

        }
    }

    override fun dispose() {
        cancel()
    }

    override fun isDisposed(): Boolean {
        return get() === SubscriptionHelper.CANCELLED
    }

    override fun request(n: Long) {
        get().request(n)
    }

    override fun cancel() {
        SubscriptionHelper.cancel(this)
    }

    override fun hasCustomOnError(): Boolean {
        return mOnError !== Functions.ON_ERROR_MISSING
    }
}
