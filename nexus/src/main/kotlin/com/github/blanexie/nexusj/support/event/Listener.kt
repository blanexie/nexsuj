package com.github.blanexie.nexusj.support.event

interface Listener {

    fun topic(): String

    suspend fun process(event: Event<*>)

}