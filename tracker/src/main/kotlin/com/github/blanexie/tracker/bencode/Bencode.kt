package com.github.blanexie.tracker.bencode

import java.nio.ByteBuffer

fun Byte.asChar(): Char {
    return Char(this.toInt())
}


internal fun benDecode(byteBuffer: ByteBuffer): BeObj {
    val byte = byteBuffer.get(byteBuffer.position())
    if (byte.asChar() == 'i') {
        return decodeInt(byteBuffer)
    }
    if (byte.asChar() == 'l') {
        return decodeList(byteBuffer)
    }
    if (byte.asChar() == 'd') {
        return decodeMap(byteBuffer)
    }
    return decodeStr(byteBuffer)
}

private fun decodeMap(byteBuffer: ByteBuffer): BeMap {
    byteBuffer.get()
    val map = mutableMapOf<BeStr, BeObj>()
    while (true) {
        val key = benDecode(byteBuffer)
        val value = benDecode(byteBuffer)
        map[key as BeStr] = value
        val byte = byteBuffer.get(byteBuffer.position())
        if (byte.asChar() == 'e') {
            byteBuffer.get()
            return BeMap(map)
        }
    }
}

private fun decodeStr(byteBuffer: ByteBuffer): BeStr {
    val bytes = arrayListOf<Byte>()
    while (true) {
        val byte = byteBuffer.get()
        if (byte.asChar() == ':') {
            val string = String(bytes.toByteArray())
            var length = string.toInt()
            val data = ByteArray(length)
            byteBuffer.get(data)
            return BeStr(String(data))
        } else {
            bytes.add(byte)
        }

    }
}


private fun decodeList(byteBuffer: ByteBuffer): BeList {
    byteBuffer.get()
    val value = arrayListOf<BeObj>()
    while (true) {
        val beObj = benDecode(byteBuffer)
        value.add(beObj)
        val byte = byteBuffer.get(byteBuffer.position())
        if (byte.asChar() == 'e') {
            byteBuffer.get()
            return BeList(value)
        }
    }
}

private fun decodeInt(byteBuffer: ByteBuffer): BeInt {
    byteBuffer.get()
    val bytes = arrayListOf<Byte>()
    while (true) {
        val byte = byteBuffer.get()
        if (byte.asChar() == 'e') {
            val toInt = String(bytes.toByteArray()).toLong()
            return BeInt(toInt)
        } else {
            bytes.add(byte)
        }
    }
}