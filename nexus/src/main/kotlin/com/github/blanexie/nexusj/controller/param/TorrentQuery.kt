package com.github.blanexie.nexusj.controller.param

data class TorrentQuery(

    /**
     * hash值
     */
    var infoHash: String? = null,

    /**
     * 标题
     */
    var title: String? = null,

    /**
     * 分类
     */
    var type: String? = null,

    /**
     * 标签
     */
    var labels: List<String>? = null,

    /**
     * 免费率
     */
    var ratio: Int? = null,

    var pageNo: Int = 1,

    var pageSize: Int = 25
)


