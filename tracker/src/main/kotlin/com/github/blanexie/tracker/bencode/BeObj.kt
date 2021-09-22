package com.github.blanexie.tracker.bencode

interface BeObj  {

    fun toBenStr(): String

    fun  getValue(): Any

}
