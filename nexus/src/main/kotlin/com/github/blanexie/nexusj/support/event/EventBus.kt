package com.github.blanexie.nexusj.support.event

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import org.slf4j.LoggerFactory


val logger = LoggerFactory.getLogger("event")


interface EventBus {

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    fun publish(event: Event<*>)

    fun close()
}

class Event<T : Any>(val topic: String, val data: T) {
    val dataType = data::class
}

class EventBusImpl : EventBus {

    private val coroutineScope: CoroutineScope = GlobalScope
    private val eventDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val exception: ((Throwable) -> Unit)? = null

    private val listeners = hashMapOf<String, Listener>()
    private val channel = Channel<Event<*>>()

    init {
        coroutineScope.launch {
            channel.consumeEach { // 消费者循环地消费消息
                launch(eventDispatcher) {
                    try {
                        listeners[it.topic]?.process(it)
                    } catch (e: Exception) {
                        exception?.invoke(e)
                    }
                }
            }
        }
    }

    override fun addListener(listener: Listener) {
        listeners[listener.topic()] = listener
    }

    override fun removeListener(listener: Listener) {
        listeners.remove(listener.topic())
    }

    override fun publish(event: Event<*>) {
        if (!channel.isClosedForSend) {
            coroutineScope.launch {
                channel.send(event)
            }
        } else {
            logger.error("", Exception("Channel is closed for send"))
        }
    }

    fun publish(topic: String, data: Any) {
        this.publish(Event(topic, data))
    }

    override fun close() {
        channel.close()
    }

}

const val uploadBytes="download-upload-bytes-num";
val eventBus = EventBusImpl()