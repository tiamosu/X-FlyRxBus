package com.xia.flyrxbus;

@SuppressWarnings("WeakerAccess")
final class TagMessage {
    public Object mEvent;
    public String mTag;

    public TagMessage(Object event, String tag) {
        mEvent = event;
        mTag = tag;
    }

    public boolean isSameType(final Class eventType, final String tag) {
        return Utils.equals(getEventType(), eventType)
                && Utils.equals(this.mTag, tag);
    }

    public Class getEventType() {
        return Utils.getClassFromObject(mEvent);
    }

    @Override
    public String toString() {
        return "event: " + mEvent + ", tag: " + mTag;
    }
}
