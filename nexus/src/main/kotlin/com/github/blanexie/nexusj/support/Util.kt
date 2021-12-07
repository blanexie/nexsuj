package com.github.blanexie.nexusj.support

import cn.hutool.cache.file.LFUFileCache

//参数1：容量，能容纳的byte数
//参数2：最大文件大小，byte数，决定能缓存至少多少文件，大于这个值不被缓存直接读取
//参数3：超时。毫秒
var fileCache = LFUFileCache(1024 * 1024 * 10, 1024 * 100, 60 * 60_000)

/**
 * 临时文件夹地址
 */
val tempDir = System.getProperty("java.io.tmpdir")


val UTF8 = Charsets.UTF_8