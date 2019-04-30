package com.xia.flyrxbus

import androidx.annotation.NonNull

/**
 * @author xia
 * @date 2018/8/1.
 */
@Suppress("unused", "UNCHECKED_CAST")
class RxBusMessage(@NonNull vararg obj: Any) {
    @JvmField
    var mObj: Any = obj[0]
    @JvmField
    var mObjs: Array<out Any> = obj
}
