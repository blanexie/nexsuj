package com.github.blanexie.event

interface Listener {

    fun topic(): String

    suspend fun process(event: Event<*>)

}