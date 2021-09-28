package com.github.blanexie.tracker.bencode

import cn.hutool.core.codec.Base64
import cn.hutool.core.util.HexUtil
import com.github.blanexie.dao.TorrentDO
import java.nio.ByteBuffer
import java.util.*


fun toBeMap(torrent: TorrentDO): BeObj {
    val beMap = hashMapOf<String, Any>()
    beMap["announce"] = torrent.announce
    torrent.announceList?.let { it ->
        beMap["announce-list"] = it
    }
    torrent.createdBy?.let {
        beMap["created by"] = it
    }
    torrent.comment?.let {
        beMap["comment"] = it
    }
    torrent.creationDate?.let {
        beMap["creation date"] = it
    }
    torrent.encoding?.let {
        beMap["encoding"] = it
    }

    val infoMap = hashMapOf<String, Any>()
    infoMap["pieces"] = torrent.pieces
    infoMap["piece length"] = torrent.pieceLength
    infoMap["name"] = torrent.name
    //单文件
    if (torrent.length != null) {
        infoMap["length"] = torrent.length!!
    } else {
        infoMap["files"] = torrent.files!!
    }
    infoMap["private"] = torrent.private
    beMap["info"] = infoMap
    return BeObj(beMap)
}

fun toTorrent(byteBuffer: ByteBuffer): TorrentDO {
    val beObj = toBeObj(byteBuffer)
    return toTorrent(beObj)
}

fun toTorrent(beMap: BeObj): TorrentDO {
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
    val infoMap = beMapData["info"] as Map<String, Any>

    val name = infoMap["name"]!!.toString()
    val pieces = infoMap["pieces"].toString()!!
    val pieceLength = infoMap["piece length"]!! as Long

    val private = infoMap["private"] as Long
    //开始处理单文件和多文件
    val length = infoMap["length"] as Long?
    val files = infoMap["files"] as List<Map<String, Any>>?

    return buildTorrent(
        announce, announceList, createdBy, comment, creationDate, encoding,
        name, pieces, pieceLength, length, private.toInt(), files
    )
}


private fun buildTorrent(
    announce: String,
    announceList: List<String>?,
    createdBy: String?,
    comment: String?,
    creationDate: Long?,
    encoding: String?,

    name: String,
    pieces: String,
    pieceLength: Long,
    length: Long?,
    private: Int,
    files: List<Map<String, Any>>?
): TorrentDO {
    val torrentDO = TorrentDO()
    torrentDO.announce = announce
    torrentDO.announceList = announceList
    torrentDO.createdBy = createdBy
    torrentDO.comment = comment
    torrentDO.creationDate = creationDate
    torrentDO.encoding = encoding
    torrentDO.name = name
    torrentDO.pieces = pieces
    torrentDO.pieceLength = pieceLength
    torrentDO.length = length
    torrentDO.private = private
    torrentDO.files = files
    return torrentDO
}

/**
 * 解码操作方法, 将种子文件转换成定义的BeMap对象
 */
 fun toBeObj(byteBuffer: ByteBuffer): BeObj {
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

fun Byte.asChar(): Char {
    return this.toInt().toChar()
}

private fun decodeMap(byteBuffer: ByteBuffer): BeObj {
    byteBuffer.get()
    val map = mutableMapOf<String, Any>()
    while (true) {
        val key = toBeObj(byteBuffer)
        val value = toBeObj(byteBuffer)
        map[key.getValue() as String] = value.getValue()
        val byte = byteBuffer.get(byteBuffer.position())
        if (byte.asChar() == 'e') {
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
        if (byte.asChar() == 'e') {
            byteBuffer.get()
            return BeObj(value)
        }
    }
}

private fun decodeStr(byteBuffer: ByteBuffer): BeObj {
    val bytes = arrayListOf<Byte>()
    while (true) {
        val byte = byteBuffer.get()
        if (byte.asChar() == ':') {
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
        if (byte.asChar() == 'e') {
            val toInt = String(bytes.toByteArray()).toLong()
            return BeObj(toInt)
        } else {
            bytes.add(byte)
        }
    }
}
