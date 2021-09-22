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

            //开始处理文件 外部字段
            torrent.announce = beMap.getMapValue("announce")!!.getValue().toString()
            beMap.getMapValue("announce-list")?.let { it ->
                if(it is BeList){
                    torrent.announceList = it.getValue().map { it.getValue().toString() }
                }
            }

            torrent.comment = beMap.getMapValue("comment")?.getValue().toString()
            torrent.creationDate = beMap.getMapValue("creation date")?.getValue() as Long
            torrent.createdBy = beMap.getMapValue("created by")?.getValue().toString()
            torrent.encoding = beMap.getMapValue("encoding")?.getValue().toString()

           //开始处理Info中字段
            torrent.info = Info()
            val infoMap = beMap.getMapValue("info")!! as BeMap
            torrent.info.name = infoMap.getMapValue("name")!!.getValue() as String
            torrent.info.pieces = infoMap.getMapValue("pieces")!!.getValue().toString().toByteArray()
            torrent.info.pieceLength = infoMap.getMapValue("piece length")!!.getValue() as Long

            //开始处理单文件和多文件
            val length = infoMap.getMapValue("length")
            if (length != null) {
                torrent.info.length = length.getValue() as Long
                torrent.info.private = infoMap.getMapValue("private")?.getValue() as Long
                torrent.info.source = infoMap.getMapValue("source")?.getValue().toString()
                torrent.info.publisher = infoMap.getMapValue("publisher")?.getValue().toString()
                torrent.info.publisherUrl = infoMap.getMapValue("publisher-url")?.getValue().toString()
            } else {
                val filesMap = infoMap.getMapValue("files") as BeList

                torrent.info.files = filesMap.getValue().map { it ->
                    val fileMap = it as BeMap
                    val fileBt = FileBt()
                    fileBt.length = fileMap.getMapValue("length")!!.getValue() as Long
                    fileBt.path = (fileMap.getMapValue("path")!! as BeList).getValue().map { it.getValue() as String }
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



