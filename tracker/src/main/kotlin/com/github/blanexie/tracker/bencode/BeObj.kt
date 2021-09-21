package com.github.blanexie.tracker.bencode

interface BeObj  {
    fun toStr(): String

    fun  getOriginal(): Any
}
