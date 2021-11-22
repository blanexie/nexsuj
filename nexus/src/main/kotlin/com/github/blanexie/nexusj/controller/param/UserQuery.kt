package com.github.blanexie.nexusj.controller.param


/**
 * 注册对象
 */
data class UserQuery(
    var id: Int? = null,
    var email: String? = null,
    var pwd: String? = null,
    var nick: String? = null,
    var avatar: String? = null,
    var sex: Int? = null,
    var code: String? = null,      //邮箱收到的验证码
    var createTime: Long? = null,
    var updateTime: Long? = null,
    var authKey: String? = null,
    var status: Int? = null,
)