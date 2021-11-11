package com.github.blanexie.nexusj.bencode

import cn.hutool.crypto.digest.DigestUtil
import com.dampcake.bencode.Bencode
import com.dampcake.bencode.Type
import com.github.blanexie.dao.TorrentDO
import java.nio.ByteBuffer

val bencode = Bencode()

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
    val infoDO = bencode.decode(info, Type.DICTIONARY)
    beMap["info"] = infoDO
    return bencode.encode(beMap)
}


fun toTorrent(beMapData: Map<String, Any>): Pair<TorrentDO, ByteArray> {
    //开始处理文件 外部字段
    val announce = beMapData["announce"] as String
    val comment = beMapData["comment"] as String?
    val creationDate = beMapData["creation date"] as Long?
    val createdBy = beMapData["created by"] as String?
    val encoding = beMapData["encoding"] as String?

    //开始处理Info中字段
    val infoMap = beMapData["info"] as Map<String, Any>
    //计算infohash值
    val infoBytes = bencode.encode(infoMap)
    val infoHash = urlEncode(DigestUtil.sha1(infoBytes))
    val name = infoMap["name"] as String

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
    return buildTorrent to infoBytes
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


fun Byte.asChar(): Char {
    return this.toInt().toChar()
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

