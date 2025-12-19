package com.data.mobile.resouce.arouter.guest


import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IntDef
import java.io.Serializable


private const val KEY_INT = "key_int"
private const val KEY_STRING = "key_string"
private const val KEY_BOOL = "key_bool"
private const val KEY_SERIALIZABLE = "key_serializable"
private const val KEY_PARCELABLE = "key_parcelable"

/**X
 * 事件消息对象
 * 用来呼叫访客的对象
 */

class VisitorCallMessage(var type: Int = MessageTypeEnum.Update) {
    var bundle = Bundle()

    //-----------添加数据对象------------
    fun put(value: Int): VisitorCallMessage {
        bundle.putInt(KEY_INT, value)
        return this
    }

    fun put(value: String): VisitorCallMessage {
        bundle.putString(KEY_STRING, value)
        return this
    }

    fun put(value: Boolean): VisitorCallMessage {
        bundle.putBoolean(KEY_BOOL, value)
        return this
    }

    fun put(value: Serializable): VisitorCallMessage {
        bundle.putSerializable(KEY_SERIALIZABLE, value)
        return this
    }

    fun put(value: Parcelable): VisitorCallMessage {
        bundle.putParcelable(KEY_PARCELABLE, value)
        return this
    }

    fun put(key: String, value: Int): VisitorCallMessage {
        bundle.putInt(key, value)
        return this
    }

    fun put(key: String, value: String): VisitorCallMessage {
        bundle.putString(key, value)
        return this
    }

    fun put(key: String, value: Boolean): VisitorCallMessage {
        bundle.putBoolean(key, value)
        return this
    }

    fun put(key: String, value: Serializable): VisitorCallMessage {
        bundle.putSerializable(key, value)
        return this
    }

    fun put(key: String, value: Parcelable): VisitorCallMessage {
        bundle.putParcelable(key, value)
        return this
    }


    //=========================获取数据对象======================================

    fun getInt(): Int {
        return bundle.getInt(KEY_INT)
    }

    fun getString(): String? {
        return bundle.getString(KEY_STRING)
    }

    fun getBoolean(): Boolean {
        return bundle.getBoolean(KEY_BOOL)
    }

    fun <T : Serializable> getSerializable(): Serializable {
        return bundle.getSerializable(KEY_SERIALIZABLE) as T
    }

    fun <T : Parcelable> getParcelable(): T? {
        return bundle.getParcelable<T>(KEY_PARCELABLE)
    }

    fun getInt(key: String): Int {
        return bundle.getInt(key)
    }

    fun getString(key: String): String? {
        return bundle.getString(key)
    }

    fun getBoolean(key: String): Boolean {
        return bundle.getBoolean(key)
    }

    fun <T : Serializable> getSerializable(key: String): Serializable {
        return bundle.getSerializable(key) as T
    }

    fun <T : Parcelable> getParcelable(key: String): T? {
        return bundle.getParcelable<T>(key)
    }
}

/**
 * 消息类型 根据这个做具体的事情
 */
@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS,
    AnnotationTarget.LOCAL_VARIABLE
)
@MustBeDocumented
@IntDef(
    MessageTypeEnum.Update,
    MessageTypeEnum.Add,
    MessageTypeEnum.Delete,
    MessageTypeEnum.Parcel,
)
@Retention(AnnotationRetention.SOURCE)
annotation class MessageTypeEnum {
    companion object {
        /*更新指令*/
        const val Update = 1

        /*增加数据指令*/
        const val Add = 2

        /*删除指令*/
        const val Delete = 3

        /*包裹 - 外部给了包裹*/
        const val Parcel = 4
    }
}