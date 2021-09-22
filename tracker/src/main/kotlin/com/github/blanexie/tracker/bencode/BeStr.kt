package com.github.blanexie.tracker.bencode

class BeStr(private val value: String) : BeObj  {

    override fun toBenStr(): String {
        return value.length.toString() + ":" + value
    }

    override fun getValue(): String {
        return this.value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeStr

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }


}
