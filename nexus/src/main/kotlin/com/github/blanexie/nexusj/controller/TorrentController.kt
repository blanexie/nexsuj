package com.github.blanexie.nexusj.controller

import cn.hutool.core.util.IdUtil
import com.github.blanexie.dao.*
import com.github.blanexie.nexusj.controller.param.Result
import com.github.blanexie.nexusj.controller.param.UserQuery
import com.github.blanexie.tracker.bencode.toBeMap
import com.github.blanexie.tracker.bencode.toTorrent
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.Serializable
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import java.nio.ByteBuffer
import java.time.LocalDateTime
import kotlin.reflect.KClass


fun Route.nexusj() {

    /**
     * 退出登录
     */
    get("/logout") {
        call.sessions.clear("user")
        call.respond(Result<String>())
    }

    /**
     * 注册
     */
    post("/signup") {
        val signUpParam = call.receive<UserQuery>()
        //TODO 判断验证码是否正确， 判断昵称和邮箱是否已经存在
        val userDO = UserDO()
        userDO.createTime = LocalDateTime.now()
        userDO.email = signUpParam.email!!
        userDO.pwd = signUpParam.pwd!!
        userDO.nick = signUpParam.nick!!
        userDO.sex = signUpParam.sex!!
        userDO.status = 0
        userDO.updateTime = LocalDateTime.now()
        userDO.authKey = IdUtil.fastSimpleUUID()
        database.userDO.add(userDO)
        userDO.pwd = ""
        call.respond(Result<UserDO>(body = userDO))
    }
    /**
     * 登录
     */
    post("/login") {
        val loginParam = call.receive<UserQuery>()
        val userDOs = database.userDO
            .filter { (it.pwd eq loginParam.pwd!!) and (it.email eq loginParam.email!!) }
            .map { it }
        if (userDOs.isEmpty()) {
            call.respond(Result<String>(403, "登录失败"))
            return@post
        }
        call.sessions.set("user", userDOs[0])
        call.respond(Result<String>())
        return@post
    }


    /**
     * 上传种子文件
     */
    post("/upload/torrent") {
        val user = call.sessions.get("user") as UserDO
        val multipartData = call.receiveMultipart()

        val reqMap = hashMapOf<String, Any>()
        multipartData.forEachPart { partData ->
            when (partData) {
                is PartData.FileItem -> {

                    val torrent = toTorrent(ByteBuffer.wrap(partData.streamProvider().readBytes()))
                    reqMap["torrent"] = torrent
                }
                is PartData.FormItem -> {
                    reqMap[partData.name!!] = partData.value
                }
                else -> {

                }
            }
        }

        val torrent = reqMap["torrent"] as TorrentDO
        torrent.userId = user.id
        torrent.status = 0
        torrent.uploadTime = LocalDateTime.now()
        torrent.type = reqMap["type"] as String
        torrent.labels = reqMap["labels"] as List<String>
        torrent.title = reqMap["title"] as String
        torrent.description = reqMap["description"] as String
        database.torrentDO.add(torrent)

        call.respond(Result<String>())
    }


    /**
     * 下载种子文件
     */
    post("/download/torrent") {
        val user = call.sessions.get("user") as UserDO

        val infoHash = call.request.queryParameters["infoHash"].toString()
        val torrentDO = database.torrentDO.first { it.pieces eq infoHash }

        if (torrentDO == null) {
            call.respond(Result<String>(code = 404, message = "下载的种子不存在"))
            return@post
        }
        //增加一条userTorrent记录
        var userTorrentDO = database.userTorrentDO.first { (it.infoHash eq infoHash) and (it.userId eq user.id) }
        if (userTorrentDO == null) {
            userTorrentDO = UserTorrentDO()
            userTorrentDO.userId = user.id
            userTorrentDO.createTime = LocalDateTime.now()
            userTorrentDO.authKey = IdUtil.fastSimpleUUID()
            userTorrentDO.status = 0
            database.userTorrentDO.add(userTorrentDO)
        }

        //构建下载者的announce
        val propsDO = database.propsDO.first {
            (it.type eq "properties") and (it.code eq "announce")
        }
        torrentDO.announce = "${propsDO.value}?auth_key=${userTorrentDO.authKey}"
        torrentDO.announceList = null
        val toBeMap = toBeMap(torrentDO)

        //返回
        call.respond(Result(body = toBeMap.toBenStr()))
    }

}
