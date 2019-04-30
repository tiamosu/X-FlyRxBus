package com.xia.rxbus;

@SuppressWarnings("WeakerAccess")
public class Config {

    public static String sMsg = "";

    public static void restoreMsg() {
        sMsg = "";
    }

    public static String appendMsg(String msg) {
        if (sMsg.equals("")) {
            sMsg = msg;
        } else {
            sMsg = sMsg + "\n" + msg;
        }
        return sMsg;
    }
}
