package com.github.blanexie.tracker.server.model

data class TrackerReq(
    val infoHash: String,
    val peerId: String,
    val port: Int,
    val left: Long,
    val numwant: Int,
    val uploaded: Long = 0,
    val downloaded: Long = 0,
    val ip: String? = null,
    val event: Event? = null
) {


}


enum class Event {
    STOPPED, COMPLETED, STARTED
}