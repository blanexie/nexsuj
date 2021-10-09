package com.github.blanexie.event

interface Listener {

    fun id():String

    suspend fun process(event: Any)
}