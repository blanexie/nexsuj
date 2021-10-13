package com.github.blanexie.nexusj.controller.param

import com.github.blanexie.dao.UserDO


data class Result(val code: Int = 20000, val message: String = "", val body: Map<String, Any>? = null)