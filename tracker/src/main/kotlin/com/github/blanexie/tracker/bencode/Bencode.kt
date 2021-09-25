package com.github.blanexie.tracker.bencode

import java.nio.ByteBuffer


internal fun toBeMap(torrent: Torrent): BeObj {
    val beMap = hashMapOf<String, Any>()
    beMap["announce"] = torrent.announce
    torrent.comment?.let {
        beMap["comment"] = it
    }
    torrent.announceList?.let { it ->
        beMap["announce-list"] = it
    }
    torrent.createdBy?.let {
        beMap["created by"] = it
    }
    torrent.creationDate?.let {
        beMap["creation date"] = it
    }
    torrent.encoding?.let {
        beMap["encoding"] = it
    }
    beMap["info"] = infoToMap(torrent.info)
    return BeObj(beMap)
}

internal fun toTorrent(byteBuffer: ByteBuffer): Torrent {
    val beObj = toBeObj(byteBuffer)
    return toTorrent(beObj)
}

internal fun toTorrent(beMap: BeObj): Torrent {
    if (beMap.type != BeType.BeMap) {
        throw Exception("BeMap类型才能生成种子文件")
    }
    val beMapData = beMap.getValue() as Map<String, Any>
    //开始处理文件 外部字段
    val announce = beMapData["announce"].toString()
    val announceList = beMapData["announce-list"] as List<String>?
    val comment = beMapData["comment"]?.toString()
    val creationDate = beMapData["creation date"] as Long?
    val createdBy = beMapData["created by"]?.toString()
    val encoding = beMapData["encoding"]?.toString()
    //开始处理Info中字段
    val infoMap = beMapData.get("info")!! as Map<String, Any>
    val info = mapToInfo(infoMap)
    return Torrent(announce, info, announceList, comment, createdBy, creationDate, encoding)
}


private fun mapToInfo(infoMap: Map<String, Any>): Info {
    val name = infoMap["name"]!!.toString()
    val pieces = infoMap["pieces"]!!.toString()
    val pieceLength = infoMap["piece length"]!! as Long

    //开始处理单文件和多文件
    val length = infoMap["length"] as Long?
    if (length != null) {
        val private = infoMap["private"] as Long?
        val source = infoMap["source"]?.toString()
        val publisher = infoMap["publisher"]?.toString()
        val publisherUrl = infoMap["publisher-url"]?.toString()
        return Info(name, pieceLength, pieces, length, null, private, source, publisher, publisherUrl)
    } else {
        val filesMap = infoMap["files"] as List<*>
        val files = filesMap.map { it as Map<*, *> }
            .map {
                val paths = it["path"] as List<String>
                val length = it["length"] as Long
                FileBt(paths, length)
            }
        return Info(name, pieceLength, pieces, length, files)
    }
}

/**
 * 解码操作方法, 将种子文件转换成定义的BeMap对象
 */
internal fun toBeObj(byteBuffer: ByteBuffer): BeObj {
    val byte = byteBuffer.get(byteBuffer.position())
    if (byte.toInt().toChar() == 'i') {
        return decodeInt(byteBuffer)
    }
    if (byte.toInt().toChar() == 'l') {
        return decodeList(byteBuffer)
    }
    if (byte.toInt().toChar() == 'd') {
        return decodeMap(byteBuffer)
    }
    return decodeStr(byteBuffer)
}

private fun decodeMap(byteBuffer: ByteBuffer): BeObj {
    byteBuffer.get()
    val map = mutableMapOf<String, Any>()
    while (true) {
        val key = toBeObj(byteBuffer)
        val value = toBeObj(byteBuffer)
        map[key.getValue() as String] = value.getValue()
        val byte = byteBuffer.get(byteBuffer.position())
        if (byte.toChar() == 'e') {
            byteBuffer.get()
            return BeObj(map)
        }
    }
}

private fun decodeList(byteBuffer: ByteBuffer): BeObj {
    byteBuffer.get()
    val value = arrayListOf<Any>()
    while (true) {
        val beObj = toBeObj(byteBuffer)
        value.add(beObj.getValue())
        val byte = byteBuffer.get(byteBuffer.position())
        if (byte.toChar() == 'e') {
            byteBuffer.get()
            return BeObj(value)
        }
    }
}

private fun decodeStr(byteBuffer: ByteBuffer): BeObj {
    val bytes = arrayListOf<Byte>()
    while (true) {
        val byte = byteBuffer.get()
        if (byte.toChar() == ':') {
            val string = String(bytes.toByteArray())
            var length = string.toInt()
            val data = ByteArray(length)
            byteBuffer.get(data)
            return BeObj(String(data))
        } else {
            bytes.add(byte)
        }
    }
}

private fun decodeInt(byteBuffer: ByteBuffer): BeObj {
    byteBuffer.get()
    val bytes = arrayListOf<Byte>()
    while (true) {
        val byte = byteBuffer.get()
        if (byte.toChar() == 'e') {
            val toInt = String(bytes.toByteArray()).toLong()
            return BeObj(toInt)
        } else {
            bytes.add(byte)
        }
    }
}

private fun infoToMap(info: Info): Map<String, Any> {
    val infoMap = hashMapOf<String, Any>()
    infoMap["pieces"] = info.pieces
    infoMap["piece length"] = info.pieceLength
    infoMap["name"] = info.name

    //单文件
    if (info.length != null) {
        infoMap["length"] = info.length!!
    } else {
        infoMap["files"] = fileBtToMap(info.files!!)
    }
    info.publisherUrl?.let {
        infoMap["publisher-url"] = it
    }
    info.publisher?.let {
        infoMap["publisher"] = it
    }
    info.source?.let {
        infoMap["source"] = it
    }
    info.private?.let {
        infoMap["private"] = it
    }
    return infoMap
}

private fun fileBtToMap(files: List<FileBt>): List<Map<String, Any>> {
    return files!!.map { it ->
        hashMapOf("length" to it.length!!, "path" to it.path!!)
    }
}
