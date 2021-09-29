package com.github.blanexie.tracker.bencode

import java.lang.Exception

class BeObj(private val obj: Any) {

    var type: BeType? = null

    init {
        type = when (obj) {
            is Number -> {
                BeType.BeInt
            }
            is ByteArray -> {
                BeType.BeByte
            }
            is String -> {
                BeType.BeStr
            }
            is List<*> -> {
                BeType.BeList
            }
            is Map<*, *> -> {
                BeType.BeMap
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }

    }

    fun toBen(): ByteArray {
        when (type) {
            BeType.BeInt -> {
                return ("i" + obj + "e").toByteArray()
            }
            BeType.BeByte -> {
                val bytes = obj as ByteArray
                return (bytes.size.toString() + ":").toByteArray().plus(bytes)
            }
            BeType.BeStr -> {
                val str = obj as String
                val bytes = str.toByteArray()
                return (bytes.size.toString() + ":").toByteArray().plus(bytes)
            }

            BeType.BeList -> {
                val list = obj as List<*>
                return list.map { BeObj(it!!) }.map { it.toBen() }.reduce { acc, bytes -> acc.plus(bytes) }
            }
            BeType.BeMap -> {
                val map = obj as Map<*, *>
                return map.map { BeObj(it.key!!).toBen().plus(BeObj(it.value!!).toBen()) }
                    .reduce { acc, bytes -> acc.plus(bytes) }
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }
    }

    fun getValue(): Any {
        return obj
    }

    override fun toString(): String {
        when (type) {
            BeType.BeInt -> {
                return obj.toString()
            }
            BeType.BeByte -> {
                val bytes = obj as ByteArray
                return String(bytes)
            }
            BeType.BeStr -> {
                return obj as String
            }
            BeType.BeList -> {
                val list = obj as List<*>
                return list.joinToString(separator = ",", prefix = "[", postfix = "]") { it.toString() }
            }
            BeType.BeMap -> {
                val map = obj as Map<*, *>
                return map.map { it.key.toString() + ":" + it.value.toString() }
                    .joinToString(separator = ",", prefix = "{", postfix = "}")
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }
    }

}

enum class BeType {
    BeInt, BeStr, BeList, BeMap, BeByte
}
