package com.github.blanexie.nexusj.controller

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.URLUtil
import com.dampcake.bencode.BencodeInputStream
import com.github.blanexie.dao.*
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
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
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
        userDO.save()
        userDO.pwd = ""
        call.respond(Result(body = mapOf("user" to userDO.properties)))
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

        userDO.pwd = ""
        val token = call.application.jwtSign(gson.toJson(userDO.properties))
        call.respond(Result(body = mapOf("token" to token)))
        return@post
    }
}


fun Route.auth() {

    get("/user/info") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user
        val roleDO: RoleDO? = RoleDO.findById(user.roleId)
        if (roleDO == null) {
            call.respond(Result.NotFoundRoleError)
            return@get
        }
        call.respond(Result(body = mapOf("user" to user.properties, "role" to roleDO.properties)))
        return@get
    }

    /**
     * 下载 文件
     */
    get("/download/torrent") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user
        val id = call.request.queryParameters["id"]!!.toInt()
        val torrentDO = TorrentDO.findById(id)

        if (torrentDO == null) {
            call.respond(Result(code = 404, message = "下载的种子不存在"))
            return@get
        }
        //增加一条userTorrent记录
        var userTorrentDO = UserTorrentDO.findByInfoHashAndUserId(torrentDO.infoHash, user.id)
        if (userTorrentDO == null) {
            userTorrentDO = buildUserTorrent(torrentDO, user.id)
            userTorrentDO.save()
        }

        // 获取下载文件位置
        val torrentPath = setting["torrent.path"]
        val outBytes = fileCache.getFileBytes("${torrentPath}/${torrentDO.infoHash}")

        val fileName = "attachment; filename='${URLUtil.encode(torrentDO.title)}.torrent'; charset=utf-8"
        //返回
        call.response.header("content-disposition", fileName)
        call.respondBytes(
            bytes = outBytes,
            contentType = ContentType.parse("application/x-bittorrent")
        )
    }
    /**
     * 上传文件
     */
    post("/upload/torrent") {
        val reqMap = receiveFrom(call.receiveMultipart())
        val torrentDO = toTorrent(reqMap)
        //查询是否已经存在
        val existTorrentDO = TorrentDO.findByInfoHash(torrentDO.infoHash)
        if (existTorrentDO != null) {
            call.respond(Result(40000, "不要上传重复的文件"))
            return@post
        }
        torrentDO.save()
        call.respond(Result())
        return@post
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
