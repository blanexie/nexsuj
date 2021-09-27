package com.github.blanexie.nexusj.controller.param

import kotlinx.serialization.Serializable
import java.time.LocalDateTime


/**
 * 注册对象
 */
@Serializable
data class UserQuery(
    var id: Int,
    var email: String,
    var pwd: String,
    var nick: String,
    var sex: Int,
    var createTime: LocalDateTime,
    var updateTime: LocalDateTime,
    var authKey: String,
    var status: Int,
    var code: String, //邮箱收到的验证码
)