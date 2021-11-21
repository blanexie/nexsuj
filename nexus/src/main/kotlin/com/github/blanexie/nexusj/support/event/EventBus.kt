package com.github.blanexie.nexusj.support.event

import com.github.blanexie.nexusj.service.UdBytesListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory


val logger = LoggerFactory.getLogger("event")

interface EventBus {

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    fun publish(event: Event<*>)

    fun publish(topic: String, data: Any)
}

class Event<T : Any>(val topic: String, val data: T) {
    val dataType = data::class
}

class EventBusImpl : EventBus {

    private val coroutineScope: CoroutineScope = GlobalScope

    private val listeners = mutableMapOf(uploadBytes to UdBytesListener())

    override fun addListener(listener: Listener) {
        //listeners[listener.topic()] = listener
    }

    override fun removeListener(listener: Listener) {
       // listeners.remove(listener.topic())
    }

    override fun publish(event: Event<*>) {
        coroutineScope.launch {
            listeners[event.topic]?.process(event)
        }
    }

    override fun publish(topic: String, data: Any) {
        this.publish(Event(topic, data))
    }
}

const val uploadBytes = "download-upload-bytes-num"

val eventBus = EventBusImpl()