package com.github.blanexie.tracker.bencode


class BeMap(private val value: Map<BeStr, BeObj>) : BeObj {

    override fun toBenStr(): String {
        val str = StringBuilder("d");
        value.forEach { (t, u) -> str.append(t.toBenStr() + u.toBenStr()) }
        return str.append("e").toString()
    }

    override fun getValue(): Map<BeStr, BeObj> {
        return this.value
    }

    fun getMapValue(key: String): BeObj? {
        return this.value[BeStr(key)]
    }

}

