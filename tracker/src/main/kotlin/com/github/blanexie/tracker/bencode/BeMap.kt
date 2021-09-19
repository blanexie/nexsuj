package com.github.blanexie.tracker.bencode


class BeMap(private val value: Map<BeStr, BeObj>) : BeObj {

    override fun toStr(): String {
        val str = StringBuilder("d");
        value.forEach { (t, u) -> str.append(t.toStr() + u.toStr()) }
        return str.append("e").toString()
    }

    override fun getValue(): Map<BeStr, BeObj> {
        return this.value
    }

}

