package com.github.blanexie.nexusj.controller.param


data class Result(val code: Int = 20000, val message: String = "", val body: Map<String, Any>? = null) {
    companion object {
        val NotFoundRoleError = Result(code = 20001, message = "角色不存在")
        val BadFileUpload = Result(code = 20002, message = "上传文件的格式不正确")

    }
}

