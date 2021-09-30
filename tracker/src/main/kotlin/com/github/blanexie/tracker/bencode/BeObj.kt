package com.github.blanexie.tracker.bencode

import java.lang.Exception
import java.nio.ByteBuffer

class BeObj(private val content: ByteArray, var type: BeType,val prefixSize: Int = 1) {

    var beInt: Long?
    var beStr: String?
    var beByte: ByteArray?
    var beList: List<Any>?
    var beMap: Map<String, Any>?

    init {
        when (type) {
            BeType.BeInt -> {
                this.beInt = String(content).toLong()
            }
            BeType.BeByte -> {
                this.beByte = content
            }
            BeType.BeStr -> {
                this.beStr = String(content)
            }
            BeType.BeList -> {
                this.beList = decodeList(content)
            }
            BeType.BeMap -> {
                val map = content as Map<*, *>
                return map.map { BeObj(it.key!!).toBen() + (BeObj(it.value!!).toBen()) }
                    .reduce { acc, bytes -> acc + bytes }
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }

    }

    private fun decodeList(content: ByteArray): List<Any> {
        val beMap = mutableMapOf<String, Any>()
        val key: String
        val value: Any
        val type = getType(content[0])
        if (type == BeType.BeStr) {
            val decodeStr = this.decodeStr(content)
            key = decodeStr.getValue() as String
            val strSize= decodeStr.content.size + 1 + decodeStr.prefixSize
            val copyInto = content.copyInto(ByteArray(content.size - strSize), 0, strSize, content.size - strSize)
            val type2 = getType(copyInto[0])


        } else {
            throw Exception("BeMap的key必须是字符串")
        }

        return value
    }

    private fun decodeList(content: ByteArray): List<Any> {
        val value = arrayListOf<Any>()
        val type = getType(content[0])
        if (type == BeType.BeInt) {
            val copyInto = content.copyInto(ByteArray(content.size - 2), 0, 1, content.size - 1)
            value.add(BeObj(copyInto, BeType.BeInt).getValue())
        }
        if (type == BeType.BeList) {
            val copyInto = content.copyInto(ByteArray(content.size - 2), 0, 1, content.size - 1)
            value.add(BeObj(copyInto, BeType.BeList).getValue())
        }
        if (type == BeType.BeMap) {
            val copyInto = content.copyInto(ByteArray(content.size - 2), 0, 1, content.size - 1)
            value.add(BeObj(copyInto, BeType.BeMap).getValue())
        }
        if (type == BeType.BeStr) {
            value.add(this.decodeStr(content))
        }
        return value
    }

    private fun decodeStr(content: ByteArray): BeObj {
        val bytes = arrayListOf<Byte>()
        for (i in 1..content.size) {
            val byte = content[i]
            if (byte.asChar() == ':') {
                val string = String(bytes.toByteArray())
                var length = string.toInt()
                val copyInto = content.copyInto(ByteArray(length), 0, i, length)
                return BeObj(copyInto, BeType.BeStr,bytes.size)
            } else {
                bytes.add(byte)
            }
        }
        throw Exception("错误的字符串格式")
    }


    private fun getType(byte: Byte): BeType {
        if (byte.toInt().toChar() == 'i') {
            return BeType.BeInt
        }
        if (byte.toInt().toChar() == 'l') {
            return BeType.BeList
        }
        if (byte.toInt().toChar() == 'd') {
            return BeType.BeMap
        }
        return BeType.BeStr
    }

    fun toBen(): ByteArray {
        when (type) {
            BeType.BeInt -> {
                return ("i" + content + "e").toByteArray()
            }
            BeType.BeByte -> {
                val bytes = content as ByteArray
                return (bytes.size.toString() + ":").toByteArray() + bytes
            }
            BeType.BeStr -> {
                val str = content as String
                val bytes = str.toByteArray()
                return (bytes.size.toString() + ":").toByteArray() + bytes
            }
            BeType.BeList -> {
                val list = content as List<*>
                return list.map { BeObj(it!!) }.map { it.toBen() }.reduce { acc, bytes -> acc + bytes }
            }
            BeType.BeMap -> {
                val map = content as Map<*, *>
                return map.map { BeObj(it.key!!).toBen() + (BeObj(it.value!!).toBen()) }
                    .reduce { acc, bytes -> acc + bytes }
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }
    }

    fun getValue(): Any {
        return content
    }

    override fun toString(): String {
        when (type) {
            BeType.BeInt -> {
                return content.toString()
            }
            BeType.BeByte -> {
                val bytes = content as ByteArray
                return String(bytes)
            }
            BeType.BeStr -> {
                return content as String
            }
            BeType.BeList -> {
                val list = content as List<*>
                return list.joinToString(separator = ",", prefix = "[", postfix = "]") { it.toString() }
            }
            BeType.BeMap -> {
                val map = content as Map<*, *>
                return map.map { it.key.toString() + ":" + it.value.toString() }
                    .joinToString(separator = ",", prefix = "{", postfix = "}")
            }
            else -> throw Exception("传入正确的Bencode编码类型")
        }
    }

}

enum class BeType {
    BeInt, BeStr, BeList, BeMap, BeByte, Bencode
}
