package com.github.blanexie.tracker.bencode

class Torrent {

    var announce: String? = null
    var announceList: ArrayList<String>? = null
    var comment: String? = null
    var createdBy: String? = null
    var createDate: Long? = null
    var info: Info? = null
    var encode: String? = null

}

class Info {
    var name: String? = null
    var pieceLength: Long? = null
    var pieces: ByteArray? = null
    var length: Long? = null
    var files: Array<FileBt>? = null
    var private: Long? = null
    var source: String? = null

}

class FileBt {

    var path: Array<String>? = null
    var length: Long? = null
}



