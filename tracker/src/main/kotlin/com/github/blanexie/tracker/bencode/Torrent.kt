package com.github.blanexie.tracker.bencode


/**
 * 种子文件对象
 */
class Torrent {

    /**
     * 种子的方法， 由种子对象转换成BeMap
     */
    fun toBeMap(): BeMap {
        val beMap = hashMapOf<BeStr, BeObj>()
        beMap[BeStr("announce")] = BeStr(this.announce!!)

        this.comment?.let {
            beMap[BeStr("comment")] = BeStr(it)
        }

        this.announceList?.let { it ->
            beMap[BeStr("announce-list")] = BeList(it.map { BeStr(it) })
        }

        this.createdBy?.let {
            beMap[BeStr("created by")] = BeStr(it)
        }

        this.creationDate?.let {
            beMap[BeStr("creation date")] = BeInt(it)
        }
        this.encoding?.let {
            beMap[BeStr("encoding")] = BeStr(it)
        }

        val infoMap = this.info!!.let {
            val map = hashMapOf<BeStr, BeObj>()
            beMap[BeStr("info")] = BeMap(map)
            map
        }

        infoMap[BeStr("pieces")] = BeStr(String(info.pieces!!))
        infoMap[BeStr("piece length")] = BeInt(info.pieceLength!!)
        infoMap[BeStr("name")] = BeStr(info.name!!)

        //单文件
        if (this.info.length != null) {
            infoMap[BeStr("length")] = BeInt(info.length!!)
        } else {

            val files = info.files!!.map { it ->
                val beMap = hashMapOf<BeStr, BeObj>()
                beMap[BeStr("length")] = BeInt(it.length!!)
                val paths = it.path!!.map {
                    BeStr(it)
                }
                beMap[BeStr("path")] = BeList(paths)
                BeMap(beMap)
            }
            infoMap[BeStr("files")] = BeList(files)
        }

        info.publisherUrl?.let {
            infoMap[BeStr("publisher-url")] = BeStr(it)
        }

        info.publisher?.let {
            infoMap[BeStr("publisher")] = BeStr(it)
        }

        info.source?.let {
            infoMap[BeStr("source")] = BeStr(it)
        }

        info.private?.let {
            infoMap[BeStr("private")] = BeInt(it)
        }

        return BeMap(beMap)
    }


    companion object {
        /**
         * 静态方法， 由BeMap转换成种子对象
         */
        fun build(beMap: BeMap): Torrent {
            val torrent = Torrent()
            torrent.announce = beMap.getValue("announce")!!.getOriginal().toString()
            val value = beMap.getValue("announce-list")?.getOriginal()
            if (value is BeList) {
                torrent.announceList = value.getOriginal().map { it.getOriginal().toString() }
            }
            torrent.comment = beMap.getValue("comment")?.getOriginal().toString()
            torrent.creationDate = beMap.getValue("creation date")?.getOriginal() as Long
            torrent.createdBy = beMap.getValue("created by")?.getOriginal().toString()
            torrent.encoding = beMap.getValue("encoding")?.getOriginal().toString()
            torrent.info = Info()
            val infoMap = beMap.getValue("info")!! as BeMap
            torrent.info.name = infoMap.getValue("name")!!.getOriginal() as String
            torrent.info.pieces = infoMap.getValue("pieces")!!.getOriginal().toString().toByteArray()
            torrent.info.pieceLength = infoMap.getValue("piece length")!!.getOriginal() as Long

            val length = infoMap.getValue("length")
            if (length != null) {
                torrent.info.length = length.getOriginal() as Long
                torrent.info.private = infoMap.getValue("private")?.getOriginal() as Long
                torrent.info.source = infoMap.getValue("source")?.getOriginal().toString()
                torrent.info.publisher = infoMap.getValue("publisher")?.getOriginal().toString()
                torrent.info.publisherUrl = infoMap.getValue("publisher-url")?.getOriginal().toString()
            } else {
                val filesMap = infoMap.getValue("files") as BeList
                torrent.info.files = filesMap.getOriginal().map { it ->
                    val fileMap = it as BeMap
                    val fileBt = FileBt()
                    fileBt.length = fileMap.getValue("length")!!.getOriginal() as Long
                    fileBt.path =
                        (fileMap.getValue("path")!! as BeList).getOriginal().map { it.getOriginal() as String }
                    fileBt
                }
            }
            return torrent
        }
    }

    var announce: String? = null
    var announceList: List<String>? = null
    var comment: String? = null
    var createdBy: String? = null
    var creationDate: Long? = null
    var info: Info = Info()
    var encoding: String? = null

}

class Info {
    var name: String? = null
    var pieceLength: Long? = null
    var pieces: ByteArray? = null
    var length: Long? = null
    var files: List<FileBt>? = null
    var private: Long? = null
    var source: String? = null

    var publisher: String? = null
    var publisherUrl: String? = null


}

class FileBt {

    var path: List<String>? = null
    var length: Long? = null
}



