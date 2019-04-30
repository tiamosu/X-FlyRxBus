package com.xia.flyrxbus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

@SuppressWarnings("WeakerAccess")
final class CacheUtils {

    private final Map<Class, List<TagMessage>> stickyEventsMap = new ConcurrentHashMap<>();

    private final Map<Object, List<Disposable>> disposablesMap = new ConcurrentHashMap<>();

    public static CacheUtils getInstance() {
        return Holder.CACHE_UTILS;
    }

    private static class Holder {
        private static final CacheUtils CACHE_UTILS = new CacheUtils();
    }

    public void addStickyEvent(final TagMessage stickyEvent) {
        final Class eventType = stickyEvent.getEventType();
        if (eventType == null) {
            return;
        }
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            if (stickyEvents == null) {
                stickyEvents = new ArrayList<>();
                stickyEvents.add(stickyEvent);
                stickyEventsMap.put(eventType, stickyEvents);
            } else {
                final int indexOf = stickyEvents.indexOf(stickyEvent);
                if (indexOf == -1) {
                    // 不存在直接插入
                    stickyEvents.add(stickyEvent);
                } else {// 存在则覆盖
                    stickyEvents.set(indexOf, stickyEvent);
                }
            }
        }
    }

    public TagMessage findStickyEvent(final Class eventType, final String tag) {
        if (eventType == null) {
            return null;
        }
        synchronized (stickyEventsMap) {
            final List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            if (stickyEvents == null) {
                return null;
            }
            final int size = stickyEvents.size();
            TagMessage res = null;
            for (int i = size - 1; i >= 0; --i) {
                final TagMessage stickyEvent = stickyEvents.get(i);
                if (stickyEvent.isSameType(eventType, tag)) {
                    res = stickyEvents.get(i);
                    break;
                }
            }
            return res;
        }
    }

    void addDisposable(Object subscriber, Disposable disposable) {
        if (subscriber == null) {
            return;
        }
        synchronized (disposablesMap) {
            List<Disposable> list = disposablesMap.get(subscriber);
            if (list == null) {
                list = new ArrayList<>();
                disposablesMap.put(subscriber, list);
            }
            list.add(disposable);
        }
    }

    void removeDisposables(final Object subscriber) {
        if (subscriber == null) {
            return;
        }
        synchronized (disposablesMap) {
            final List<Disposable> disposables = disposablesMap.get(subscriber);
            if (disposables == null) {
                return;
            }
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            disposables.clear();
            disposablesMap.remove(subscriber);
        }
    }
}
