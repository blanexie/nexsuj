package com.github.blanexie.nexusj.controller

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.URLUtil
import com.dampcake.bencode.BencodeInputStream
import com.github.blanexie.dao.*
import com.github.blanexie.nexusj.bencode.toBeMap
import com.github.blanexie.nexusj.bencode.toTorrent
import com.github.blanexie.nexusj.controller.param.Result
import com.github.blanexie.nexusj.controller.param.UserQuery
import com.github.blanexie.nexusj.support.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.*
import org.ktorm.entity.add
import org.ktorm.entity.filter
import org.ktorm.entity.first
import org.ktorm.entity.firstOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import kotlin.collections.set

val logger: Logger = LoggerFactory.getLogger("NexusController")!!

fun Route.notAuth() {

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
        userDO.avatar = signUpParam.avatar
        userDO.sex = signUpParam.sex!!
        userDO.status = 0
        userDO.updateTime = LocalDateTime.now()
        userDO.authKey = IdUtil.fastSimpleUUID()
        database().userDO.add(userDO)
        userDO.pwd = ""
        call.respond(Result(body = mapOf("user" to userDO)))
    }

    /**
     * 登录
     */
    post("/login") {
        val loginParam = call.receive<UserQuery>()
        val userDO = database().userDO.firstOrNull { (it.pwd eq loginParam.pwd!!) and (it.email eq loginParam.email!!) }
        if (userDO == null) {
            call.respond(Result(403, "登录失败"))
            return@post
        }

        val userStr =
            "{'id':${userDO.id} ,'authKey':'${userDO.authKey}','avatar':'${userDO.avatar}', 'roleId':${userDO.roleId},'createTime':'${userDO.createTime.format(dateFormat)}', 'nick':'${userDO.nick}', 'email':'${userDO.email}','sex':${userDO.sex}  }"

        val token = call.application.jwtSign(userStr)
        call.respond(Result(body = mapOf("token" to token)))
        return@post
    }
}


fun Route.auth() {

    get("/user/info") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user
        val roleDO:RoleDO = database().roleDO.first { it.id eq user.roleId }
        call.respond(Result(body = mapOf("user" to user, "role" to roleDO)))
        return@get
    }

    /**
     * 下载 文件
     */
    get("/download/torrent") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user
        val id = call.request.queryParameters["id"]!!.toInt()
        val torrentDO = database().torrentDO.first { it.id eq id }

        if (torrentDO == null) {
            call.respond(Result(code = 404, message = "下载的种子不存在"))
            return@get
        }
        //增加一条userTorrent记录
        var userTorrentDO =
            database().userTorrentDO.firstOrNull { (it.infoHash eq torrentDO.infoHash) and (it.userId eq user.id) }
        if (userTorrentDO == null) {
            userTorrentDO = buildUserTorrent(torrentDO, user.id)
            database().userTorrentDO.add(userTorrentDO)
        }

        //构建下载者的announce
        val announceUrl = setting["pt.announce.url"]
        torrentDO.announce = "${announceUrl}?auth_key=${userTorrentDO.authKey}"

        val info = database().from(Info).select(Info.infoHash, Info.info)
            .where { Info.infoHash eq torrentDO.infoHash }.limit(1)
            .map { it.getBytes(2) }
            .last()

        val fileName = "attachment; filename='${URLUtil.encode(torrentDO.name)}.torrent' ; charset=utf-8"
        //返回
        call.response.header("content-disposition", fileName)
        call.respondBytes(
            bytes = toBeMap(torrentDO, info!!),
            contentType = ContentType.parse("application/x-bittorrent")
        )
    }
    /**
     * 上传文件
     */
    post("/upload/torrent") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user!!
        val reqMap = receiveFrom(call.receiveMultipart())
        val torrentFile = toTorrent(reqMap, user.id)
        val torrent = torrentFile.first
        //查询是否已经存在
        val existTorrentDO = database().torrentDO.filter { it.infoHash eq torrent.infoHash }
            .firstOrNull()
        if (existTorrentDO != null) {
            call.respond(Result(40000, "不要上传重复的文件"))
            return@post
        }

        database().useTransaction {
            try {
                database().infoDO.add(torrentFile.second)
                database().torrentDO.add(torrent)
                it.commit()
            } catch (e: Exception) {
                it.rollback()
                logger.error("", e)
            }
        }
        call.respond(Result())
    }
}

suspend fun receiveFrom(multipartData: MultiPartData): Map<String, Any> {
    val reqMap = hashMapOf<String, Any>()
    multipartData.forEachPart { partData ->
        when (partData) {
            is PartData.FileItem -> {
                reqMap["torrent"] =
                    BencodeInputStream(partData.streamProvider(), charset("utf8"), true).readDictionary()
            }
            is PartData.FormItem -> {
                reqMap[partData.name!!] = partData.value
            }
            else -> {

            }
        }
    }
    return reqMap
}


private fun buildUserTorrent(
    torrentDO: TorrentDO,
    userId: Int
): UserTorrentDO {
    val userTorrentDO = UserTorrentDO()
    userTorrentDO.infoHash = torrentDO.infoHash
    userTorrentDO.userId = userId
    userTorrentDO.createTime = LocalDateTime.now()
    userTorrentDO.authKey = IdUtil.fastSimpleUUID()
    userTorrentDO.status = 0
    return userTorrentDO
}
