package com.github.blanexie.event

import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach

interface EventBus<T> {

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    fun publish(event: T)

    fun close();

}

class EventBusImpl<T> : EventBus<T> {

    private val channel = Channel<T>()
    private val coroutineScope: CoroutineScope = GlobalScope
    private val eventDispatcher: CoroutineDispatcher = Dispatchers.Default
    private val exception: ((Throwable) -> Unit)? = null

    @OptIn(InternalAPI::class)
    val listeners =Copy<String, Listener>()

    init {
        coroutineScope.launch {
            channel.consumeEach { // 消费者循环地消费消息
                launch(eventDispatcher) {
                    try {

                    } catch (e: Exception) {
                        exception?.invoke(e)
                    }
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    override fun addListener(listener: Listener) {
        listeners.put(listener.id(),listener)
    }

    @OptIn(InternalAPI::class)
    override fun removeListener(listener: Listener) {
        listeners.remove(listener.id())
    }


    override fun publish(event: T) {
        if (!channel.isClosedForSend) {
            coroutineScope.launch {
                channel.send(event)
            }
        } else {
            println("Channel is closed for send")
        }
    }

    override fun close() {
        channel.close()
    }

}