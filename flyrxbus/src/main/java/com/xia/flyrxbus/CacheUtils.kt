package com.xia.flyrxbus

import androidx.annotation.NonNull
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object CacheUtils {

    private val STICKY_EVENT_MAP = ConcurrentHashMap<Class<*>, MutableList<TagMessage>?>()
    private val DISPOSABLE_MAP = ConcurrentHashMap<Any, MutableList<Disposable>?>()

    @JvmStatic
    fun addStickyEvent(@NonNull event: Any, @NonNull tag: String) {
        val eventType = Utils.getClassFromObject(event) ?: return
        var stickyEvents: MutableList<TagMessage>? = STICKY_EVENT_MAP[eventType]
        if (stickyEvents == null) {
            stickyEvents = ArrayList()
            stickyEvents.add(TagMessage(event, tag))
            STICKY_EVENT_MAP[eventType] = stickyEvents
        } else {
            val size = stickyEvents.size
            for (i in size - 1 downTo 0) {
                val tmp = stickyEvents[i]
                if (tmp.isSameType(eventType, tag)) {
                    Utils.logW("The sticky event already added.")
                    return
                }
            }
            stickyEvents.add(TagMessage(event, tag))
        }
    }

    @JvmStatic
    fun removeStickyEvent(@NonNull event: Any, @NonNull tag: String) {
        val eventType = Utils.getClassFromObject(event) ?: return
        val stickyEvents = STICKY_EVENT_MAP[eventType] ?: return
        val size = stickyEvents.size
        for (i in size - 1 downTo 0) {
            val stickyEvent = stickyEvents[i]
            if (stickyEvent.isSameType(eventType, tag)) {
                stickyEvents.removeAt(i)
                break
            }
        }
        if (stickyEvents.size == 0) {
            STICKY_EVENT_MAP.remove(eventType)
        }
    }

    @JvmStatic
    fun findStickyEvent(@NonNull eventType: Class<*>, @NonNull tag: String): TagMessage? {
        val stickyEvents = STICKY_EVENT_MAP[eventType] ?: return null
        val size = stickyEvents.size
        var res: TagMessage? = null
        for (i in size - 1 downTo 0) {
            val stickyEvent = stickyEvents[i]
            if (stickyEvent.isSameType(eventType, tag)) {
                res = stickyEvents[i]
                break
            }
        }
        return res
    }

    @JvmStatic
    fun addDisposable(@NonNull subscriber: Any, disposable: Disposable) {
        var list: MutableList<Disposable>? = DISPOSABLE_MAP[subscriber]
        if (list == null) {
            list = ArrayList()
            DISPOSABLE_MAP[subscriber] = list
        }
        list.add(disposable)
    }

    @JvmStatic
    fun removeDisposables(@NonNull subscriber: Any) {
        val disposables = DISPOSABLE_MAP[subscriber] ?: return
        for (disposable in disposables) {
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
        disposables.clear()
        DISPOSABLE_MAP.remove(subscriber)
    }
}
