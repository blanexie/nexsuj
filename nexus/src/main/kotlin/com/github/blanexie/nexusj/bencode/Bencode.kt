package com.github.blanexie.nexusj.bencode

import cn.hutool.crypto.digest.DigestUtil
import com.dampcake.bencode.Bencode
import com.dampcake.bencode.Type
import com.github.blanexie.dao.InfoDO
import com.github.blanexie.dao.TorrentDO
import com.github.blanexie.nexusj.support.gson
import io.ktor.util.*
import java.nio.ByteBuffer
import java.time.LocalDateTime

val bencode = Bencode(charset("utf8"), true)

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


fun toTorrent(reqMap: Map<String, Any>, userId: Int): Pair<TorrentDO, InfoDO> {
    val beMapData = reqMap["torrent"] as Map<String, Any>
    val torrent = TorrentDO()
    //开始处理文件 外部字段
    torrent.announce = beMapData["announce"].toStr()
    torrent.comment = beMapData["comment"].toStr()
    torrent.creationDate = beMapData["creation date"] as Long
    torrent.createdBy = beMapData["created by"].toStr()
    torrent.encoding = beMapData["encoding"].toStr()
    //开始处理Info中字段
    val infoMap = beMapData["info"] as Map<String, Any>
    //计算infohash值
    val infoBytes = bencode.encode(infoMap)
    torrent.infoHash =  DigestUtil.sha1Hex(infoBytes)
    torrent.name = infoMap["name"].toStr()
    torrent.private = (infoMap["private"] as Long).toInt()
    //开始处理单文件和多文件
    val length = infoMap["length"] as Long?
    torrent.files = infoMap["files"] as List<Map<String, Any>>?
    torrent.size = if (length == null) {
        val pieces = infoMap["pieces"] as ByteArray
        val pieceLength = infoMap["piece length"] as Long
        (pieces.size / 20) * pieceLength
    } else {
        length
    }
    torrent.userId = userId
    torrent.status = 0
    torrent.uploadTime = LocalDateTime.now()
    torrent.type = reqMap["type"] as String
    torrent.labels = gson.fromJson<List<String>>(reqMap["labels"] as String, List::class.java)
    torrent.title = reqMap["title"] as String

    val infoDO = InfoDO()
    infoDO.info = infoBytes
    infoDO.description = reqMap["description"] as String
    infoDO.infoHash = torrent.infoHash
    return torrent to infoDO
}

private fun Any?.toStr(): String {
    if (this is ByteBuffer) {
        return String(this.array())
    } else if (this == null) {
        return ""
    } else {
        return this.toString()
    }
}

@InternalAPI
fun ByteBuffer.toStr(): String {
    return Charsets.UTF_8.decode(this).toString()
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

