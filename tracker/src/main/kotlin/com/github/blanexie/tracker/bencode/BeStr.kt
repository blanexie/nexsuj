package com.github.blanexie.tracker.bencode

class BeStr(private val value: String) : BeObj  {

    override fun toStr(): String {
        return value.length.toString() + ":" + value
    }

    override fun getValue(): String {
        return this.value
    }

}
