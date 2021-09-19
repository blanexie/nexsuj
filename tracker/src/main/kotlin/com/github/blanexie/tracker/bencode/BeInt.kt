package com.github.blanexie.tracker.bencode

class BeInt(private val value: Long) : BeObj  {

    override fun toStr(): String {
        return "i" + value + "e"
    }

    override  fun getValue(): Long {
        return this.value
    }

}