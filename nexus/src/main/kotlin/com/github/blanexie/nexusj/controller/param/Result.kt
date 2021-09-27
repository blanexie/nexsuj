package com.github.blanexie.nexusj.controller.param

import kotlinx.serialization.Serializable

@Serializable
data class Result<T>(val code: Int = 200, val message: String = "", val body: T? = null)