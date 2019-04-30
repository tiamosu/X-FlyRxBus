package com.xia.flyrxbus

import androidx.annotation.NonNull
import io.reactivex.internal.functions.ObjectHelper

class TagMessage(@NonNull @JvmField var mEvent: Any, @NonNull @JvmField var mTag: String) {

    fun isSameType(eventType: Class<*>, tag: String): Boolean {
        return ObjectHelper.equals(getEventType(), eventType) && ObjectHelper.equals(this.mTag, tag)
    }

    fun getEventType(): Class<*>? {
        return Utils.getClassFromObject(mEvent)
    }

    override fun toString(): String {
        return "event: $mEvent, tag: $mTag"
    }
}
