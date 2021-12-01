package com.github.blanexie.nexusj.bencode

import cn.hutool.core.io.FileUtil
import cn.hutool.crypto.digest.DigestUtil
import com.dampcake.bencode.Bencode
import com.github.blanexie.dao.TorrentDO
import com.github.blanexie.nexusj.support.UTF8
import com.github.blanexie.nexusj.support.gson
import com.github.blanexie.nexusj.support.setting
import com.github.blanexie.nexusj.support.tempDir
import io.ktor.util.*
import java.io.File
import java.nio.ByteBuffer
import java.time.LocalDateTime

val bencode = Bencode(charset("utf8"), true)


fun toTorrent(reqMap: Map<String, Any>): TorrentDO {
    val torrentMap = reqMap["torrent"] as MutableMap<String, Any>
    //更新tracker url
    torrentMap["announce"] = setting["pt.announce.url"]!!

    val torrent = TorrentDO()
    //开始处理Info中字段
    val infoMap = torrentMap["info"] as MutableMap<String, Any>
    //TODO 修改下infoMap中的信息，
    //计算infohash值
    val infoBytes = bencode.encode(infoMap)
    torrent.infoHash = DigestUtil.sha1Hex(infoBytes)
    torrentMap["info_hash"] = torrent.infoHash
    torrent.name = UTF8.decode((infoMap["name"] as ByteBuffer)).toString()
    torrent.title = reqMap["title"] as String
    torrent.type = reqMap["type"] as String
    torrent.labels = gson.fromJson<List<String>>(reqMap["labels"] as String, List::class.java)
    torrent.coverPath = reqMap["coverPath"] as String
    torrent.imgList = reqMap["imgList"] as List<String>
    torrent.description = reqMap["description"] as String


    val length = infoMap["length"] as Long?
    //根据 length 来判断是单文件还是多文件
    if (length == null) {
        //多文件
        val pieces = infoMap["pieces"] as ByteBuffer
        val pieceLength = infoMap["piece length"] as Long
        torrent.size = (pieces.array().size / 20) * pieceLength
        //开始处理单文件和多文件
        torrent.files = infoMap["files"] as List<Map<String, Any>>?
        torrent.files?.forEach { it ->
            it as MutableMap<String, Any>
            val paths = it["path"] as List<*>
            val map = paths.map { it as ByteBuffer }.map { UTF8.decode(it).toString() }
            it["path"] = map
        }
    } else {
        //单文件
        torrent.size = length
    }


    torrent.uploadTime = LocalDateTime.now()
    torrent.status = 0
    torrent.ration = 1
    torrent.rationTime =  LocalDateTime.now().plusDays(3)

    val torrentPath = setting["torrent.path"]

    //保存种子文件
    FileUtil.writeBytes(bencode.encode(torrentMap), "${torrentPath}/${torrent.infoHash}/${torrent.infoHash}")
    //保存图片文件
    torrent.imgList?.forEach {
        File("$tempDir/$it").renameTo(File("${torrentPath}/${torrent.infoHash}/$it"))
    }

    return torrent
}
