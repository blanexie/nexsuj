package com.github.blanexie.tracker.bencode


/**
 * 种子文件对象
 */
data class Torrent(
    var announce: String,
    var info: Info,
    var announceList: List<String>? = null,
    var comment: String? = null,
    var createdBy: String? = null,
    var creationDate: Long? = null,
    var encoding: String? = null,
)

data class Info(
    var name: String,
    var pieceLength: Long,
    var pieces: String,
    var length: Long?,
    var files: List<FileBt>?=null,
    var private: Long?=null,
    var source: String?=null,
    var publisher: String?=null,
    var publisherUrl: String?=null,
)

data class FileBt(
    var path: List<String>,
    var length: Long
)



