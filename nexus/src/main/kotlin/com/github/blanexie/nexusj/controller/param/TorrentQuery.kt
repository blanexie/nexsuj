package com.github.blanexie.nexusj.controller.param

import com.github.blanexie.dao.TorrentDO

data class TorrentQuery(
    val torrentDO: TorrentDO,    // 种子
    val type: String,            //	分类
    val labels: List<String>,    //	标签
    val description: String,       //	简介
    val title: String            // 标题
)