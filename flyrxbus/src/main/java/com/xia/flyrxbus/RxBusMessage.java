package com.xia.flyrxbus;

/**
 * @author xia
 * @date 2018/8/1.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RxBusMessage {
    public Object mObj;
    public Object[] mObjs;

    public RxBusMessage(Object... obj) {
        this.mObj = obj[0];
        this.mObjs = obj;
    }
}
