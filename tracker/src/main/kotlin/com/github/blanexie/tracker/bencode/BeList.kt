package com.github.blanexie.tracker.bencode

class BeList(private val value: List<BeObj>) : BeObj {

    override fun toStr(): String {
        val str = StringBuilder("l")
        this.value.forEach { str.append(it.toStr()) }
        return str.append("e").toString()
    }

    override fun getOriginal(): List<BeObj> {
        return this.value
    }

}