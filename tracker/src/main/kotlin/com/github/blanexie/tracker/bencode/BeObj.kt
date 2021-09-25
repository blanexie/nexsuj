package com.github.blanexie.tracker.bencode

import java.lang.Exception

class BeObj(private val obj: Any) {

    var type: BeType? = null

    init {
        when (obj) {
            is Number -> {
                type = BeType.BeInt
            }
            is String -> {
                type = BeType.BeStr
            }
            is List<*> -> {
                type = BeType.BeList
            }
            is Map<*, *> -> {
                type = BeType.BeMap
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }

    }

    fun toBenStr(): String {
        when (obj) {
            is Number -> {
                return "i" + obj + "e"
            }
            is String -> {
                return obj.length.toString() + ":" + obj
            }
            is List<*> -> {
                return obj.joinToString(separator = "", prefix = "l", postfix = "e") { BeObj(it!!).toBenStr() }
            }
            is Map<*, *> -> {
                return obj.map { BeObj(it.key!!).toBenStr() + BeObj(it.value!!).toBenStr() }
                    .joinToString(separator = "", prefix = "d", postfix = "e")
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }
    }

    fun getValue(): Any {
        return obj
    }

}

enum class BeType {
    BeInt, BeStr, BeList, BeMap
}
