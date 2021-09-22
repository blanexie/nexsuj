package com.github.blanexie.tracker.bencode

class BeList(private val value: List<BeObj>) : BeObj {

    override fun toBenStr(): String {
        val str = StringBuilder("l")
        this.value.forEach { str.append(it.toBenStr()) }
        return str.append("e").toString()
    }

    override fun getValue(): List<BeObj> {
        return this.value
    }

}