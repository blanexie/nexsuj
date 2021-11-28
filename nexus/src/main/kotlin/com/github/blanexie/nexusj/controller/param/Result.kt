package com.github.blanexie.nexusj.controller.param


data class Result(val code: Int = 20000, val message: String = "", val body: Map<String, Any>? = null) {
    companion object {
        val NotFoundRoleError = Result(code = 20001, message = "角色不存在")
    }
}

