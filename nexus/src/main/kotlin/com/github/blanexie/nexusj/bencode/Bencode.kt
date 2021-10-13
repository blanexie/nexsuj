package com.github.blanexie.nexusj.bencode

import cn.hutool.crypto.digest.DigestUtil
import com.github.blanexie.dao.TorrentDO
import java.nio.ByteBuffer


fun toBeMap(torrent: TorrentDO, info: ByteArray): ByteArray {
    val beMap = hashMapOf<String, Any?>()
    beMap["announce"] = torrent.announce

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
    val beObj = BeObj(beMap)
    val toBen = beObj.toBen()

    val copyInto = toBen.copyInto(ByteArray(toBen.size + info.size), 0, 0, toBen.size - 1)

    val copyInto1 = info.copyInto(copyInto, toBen.size - 1, 0)

    copyInto1[toBen.size + info.size - 1] = 'e'.code.toByte()
    return copyInto1
}


fun toTorrent(byteBuffer: ByteBuffer): Pair<TorrentDO, ByteArray> {
    val beObj = toBeObj(byteBuffer)
    return toTorrent(beObj)
}

fun toTorrent(beMap: BeObj): Pair<TorrentDO, ByteArray> {
    if (beMap.type != BeType.BeMap) {
        throw Exception("BeMap类型才能生成种子文件")
    }
    val beMapData = beMap.getValue() as Map<String, Any>
    //开始处理文件 外部字段
    val announce = (beMapData["announce"] as ByteArray).toStr()
    val comment = (beMapData["comment"] as ByteArray?)?.toStr()
    val creationDate = beMapData["creation date"] as Long?
    val createdBy = (beMapData["created by"] as ByteArray?)?.toStr()
    val encoding = (beMapData["encoding"] as ByteArray?)?.toStr()

    //开始处理Info中字段
    val infoMap = beMapData["info"] as Map<String, Any>
    //计算infohash值
    val toBen = BeObj(infoMap).toBen()
    val infoHash = urlEncode(DigestUtil.sha1(toBen))
    val name = (infoMap["name"] as ByteArray).toStr()

    val private = infoMap["private"] as Long
    //开始处理单文件和多文件
    val length = infoMap["length"] as Long?
    val files = infoMap["files"] as List<Map<String, Any>>?
    val size = if (length == null) {
        val pieces = infoMap["pieces"] as ByteArray
        val pieceLength = infoMap["piece length"] as Long
        (pieces.size / 20) * pieceLength
    } else {
        length
    }
    val buildTorrent = buildTorrent(
        announce, createdBy, comment, creationDate, encoding, infoHash, size,
        name, private.toInt(), files
    )
    return buildTorrent to toBen
}


private fun buildTorrent(
    announce: String,
    createdBy: String?,
    comment: String?,
    creationDate: Long?,
    encoding: String?,
    infoHash: String,

    size: Long,
    name: String,
    private: Int,
    files: List<Map<String, Any>>?
): TorrentDO {
    val torrentDO = TorrentDO()
    torrentDO.announce = announce
    torrentDO.createdBy = createdBy
    torrentDO.comment = comment
    torrentDO.creationDate = creationDate
    torrentDO.encoding = encoding
    torrentDO.infoHash = infoHash

    torrentDO.size = size
    torrentDO.name = name
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
        map[key.toString()] = value.getValue()
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
            return BeObj(data)
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

fun ByteArray.toString(): String {
    return String(this)
}


fun urlEncode(bytes: ByteArray): String {
    val buf = StringBuilder()
    for (b in bytes) {
        val c = b.asChar()
        if (c.code in 48..57 // 0-9
            || c.code in 65..90 // A-Z
            || c.code in 97..122 // a-z
            || c.code == 45 || c.code == 46 || c.code == 95 || c.code == 126
        ) {
            buf.append(c)
        } else {
            buf.append("%")
            val hex = Integer.toHexString(b.toInt() and 0xFF).uppercase()
            if (hex.length == 1) {
                buf.append("0")
            }
            buf.append(hex)
        }
    }
    return buf.toString()
}


fun ByteArray.toStr(): String {
    return String(this)
}