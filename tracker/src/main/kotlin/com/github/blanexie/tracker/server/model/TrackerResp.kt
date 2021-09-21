package com.github.blanexie.tracker.server.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackerResp(
    val interval: Int?=null,
    val peers: List<Peer>?=null,
    val complete: Int? = null,
    val inComplete: Int? = null,
    val failReason: String? = null
)


@Serializable
data class Peer(
    val ip: String,
    val peerId: String,
    val port: Int
)