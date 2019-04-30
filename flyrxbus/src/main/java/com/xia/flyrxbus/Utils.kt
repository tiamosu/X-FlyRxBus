package com.xia.flyrxbus

import android.util.Log
import androidx.annotation.NonNull

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object Utils {

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> getTypeClassFromParadigm(@NonNull callback: RxBus.Callback<T>): Class<T>? {
        val genericInterfaces = callback.javaClass.genericInterfaces
        var type: Type?
        type = if (genericInterfaces.size == 1) {
            genericInterfaces[0]
        } else {
            callback.javaClass.genericSuperclass
        }
        if (type is ParameterizedType) {
            val parameterizedType = type as ParameterizedType?
            type = parameterizedType?.actualTypeArguments?.get(0)
        }
        while (type is ParameterizedType) {
            type = type.rawType
        }
        type ?: return null

        var className = type.toString()
        if (className.startsWith("class ")) {
            className = className.substring(6)
        } else if (className.startsWith("interface ")) {
            className = className.substring(10)
        }
        try {
            return Class.forName(className) as Class<T>
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun getClassFromObject(@NonNull obj: Any): Class<*>? {
        val objClass = obj.javaClass
        if (objClass.isAnonymousClass || objClass.isSynthetic) {
            val genericInterfaces = objClass.genericInterfaces
            var type: Type?
            // interface
            type = if (genericInterfaces.size == 1) {
                genericInterfaces[0]
            } else {// abstract class or lambda
                objClass.genericSuperclass
            }
            while (type is ParameterizedType) {
                type = type.rawType
            }
            type ?: return null

            var className = type.toString()
            if (className.startsWith("class ")) {
                className = className.substring(6)
            } else if (className.startsWith("interface ")) {
                className = className.substring(10)
            }
            try {
                return Class.forName(className)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
        return objClass
    }

    @JvmStatic
    fun logW(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w("RxBus", msg)
        }
    }

    @JvmStatic
    fun logE(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("RxBus", msg)
        }
    }
}
