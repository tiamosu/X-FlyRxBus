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

    public void addStickyEvent(final Object event, final String tag) {
        final Class eventType = Utils.getClassFromObject(event);
        if (eventType == null) {
            return;
        }
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            if (stickyEvents == null) {
                stickyEvents = new ArrayList<>();
                stickyEvents.add(new TagMessage(event, tag));
                stickyEventsMap.put(eventType, stickyEvents);
            } else {
                final int size = stickyEvents.size();
                for (int i = size - 1; i >= 0; --i) {
                    final TagMessage tmp = stickyEvents.get(i);
                    if (tmp.isSameType(eventType, tag)) {
                        Utils.logW("The sticky event already added.");
                        return;
                    }
                }
                stickyEvents.add(new TagMessage(event, tag));
            }
        }
    }

    public void removeStickyEvent(final Object event, final String tag) {
        final Class eventType = Utils.getClassFromObject(event);
        if (eventType == null) {
            return;
        }
        synchronized (stickyEventsMap) {
            List<TagMessage> stickyEvents = stickyEventsMap.get(eventType);
            if (stickyEvents == null) {
                return;
            }
            final int size = stickyEvents.size();
            for (int i = size - 1; i >= 0; --i) {
                final TagMessage stickyEvent = stickyEvents.get(i);
                if (stickyEvent.isSameType(eventType, tag)) {
                    stickyEvents.remove(i);
                    break;
                }
            }
            if (stickyEvents.size() == 0) {
                stickyEventsMap.remove(eventType);
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
