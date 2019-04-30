package com.xia.flyrxbus;

import io.reactivex.Scheduler;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class RxBusManager {

    public static void post(final Object event) {
        RxBus.getDefault().post(event);
    }

    public static void post(final Object event, final String tag) {
        RxBus.getDefault().post(event, tag);
    }

    public static void postSticky(final Object event) {
        RxBus.getDefault().postSticky(event);
    }

    public static void postSticky(final Object event, final String tag) {
        RxBus.getDefault().postSticky(event, tag);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribe(subscriber, callback);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final Scheduler scheduler,
                                     final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribe(subscriber, scheduler, callback);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final String tag,
                                     final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribe(subscriber, tag, callback);
    }

    public static <T> void subscribe(final Object subscriber,
                                     final String tag,
                                     final Scheduler scheduler,
                                     final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribe(subscriber, tag, scheduler, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribeSticky(subscriber, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final Scheduler scheduler,
                                           final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribeSticky(subscriber, scheduler, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final String tag,
                                           final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribeSticky(subscriber, tag, callback);
    }

    public static <T> void subscribeSticky(final Object subscriber,
                                           final String tag,
                                           final Scheduler scheduler,
                                           final RxBus.Callback<T> callback) {
        RxBus.getDefault().subscribeSticky(subscriber, tag, scheduler, callback);
    }

    public static <T> void subscribeWithTags(final Object subscriber,
                                             final RxBus.Callback<T> callback,
                                             final String... tags) {
        subscribeWithTags(subscriber, null, callback, tags);
    }

    public static <T> void subscribeWithTags(final Object subscriber,
                                             final Scheduler scheduler,
                                             final RxBus.Callback<T> callback,
                                             final String... tags) {
        if (tags == null || tags.length == 0) {
            throw new RuntimeException("Tags is empty,you should set the tags");
        }
        for (String tag : tags) {
            RxBus.getDefault().subscribe(subscriber, tag, scheduler, callback);
        }
    }

    public static <T> void subscribeStickyWithTags(final Object subscriber,
                                                   final RxBus.Callback<T> callback,
                                                   final String... tags) {
        subscribeStickyWithTags(subscriber, null, callback, tags);
    }

    public static <T> void subscribeStickyWithTags(final Object subscriber,
                                                   final Scheduler scheduler,
                                                   final RxBus.Callback<T> callback,
                                                   final String... tags) {
        if (tags == null || tags.length == 0) {
            throw new RuntimeException("Tags is empty,you should set the tags");
        }
        for (String tag : tags) {
            RxBus.getDefault().subscribeSticky(subscriber, tag, scheduler, callback);
        }
    }

    public static void unregister(final Object subscriber) {
        RxBus.getDefault().unregister(subscriber);
    }
}
