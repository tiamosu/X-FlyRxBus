package com.xia.rxbus

object Config {
    var sMsg = ""

    fun restoreMsg() {
        sMsg = ""
    }

    fun appendMsg(msg: String): String {
        sMsg = if (sMsg == "") {
            msg
        } else {
            sMsg + "\n" + msg
        }
        return sMsg
    }
}
