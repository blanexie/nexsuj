package com.github.blanexie.nexusj.controller

import cn.hutool.core.util.IdUtil
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
        userDO.sex = signUpParam.sex!!
        userDO.status = 0
        userDO.updateTime = LocalDateTime.now()
        userDO.authKey = IdUtil.fastSimpleUUID()
        database.userDO.add(userDO)
        userDO.pwd = ""
        call.respond(Result(body = mapOf("user" to userDO)))
    }

    /**
     * 登录
     */
    post("/login") {
        val loginParam = call.receive<UserQuery>()
        val userDO = database.userDO
            .first { (it.pwd eq loginParam.pwd!!) and (it.email eq loginParam.email!!) }
        if (userDO == null) {
            call.respond(Result(403, "登录失败"))
            return@post
        }

        val userStr =
            "{'id':${userDO.id} ,'authKey':'${userDO.authKey}','createTime':'${userDO.createTime.format(dateFormat)}', 'nick':'${userDO.nick}', 'email':'${userDO.email}','sex':${userDO.sex}  }"

        val token = call.application.jwtSign(userStr)
        call.respond(Result(body = mapOf("token" to token)))
        return@post
    }


    /**
     * 下载 文件
     */
    get("/download/torrent") {
       // val principal = call.authentication.principal<UserPrincipal>()!!
        val u =UserDO()
        u.email="abc@qq.com"
        u.id=1
        u.authKey="9c69a0b7707840488e793019e9e8e42b"
        u.nick="abc"
        u.sex=1
        u.status= 0
        val user = u

        val id = call.request.queryParameters["id"]!!.toInt()
        val torrentDO = database.torrentDO.first { it.id eq id }

        if (torrentDO == null) {
            call.respond(Result(code = 404, message = "下载的种子不存在"))
            return@get
        }
        //增加一条userTorrent记录
        var userTorrentDO =
            database.userTorrentDO.firstOrNull { (it.infoHash eq torrentDO.infoHash) and (it.userId eq user.id) }
        if (userTorrentDO == null) {
            userTorrentDO = UserTorrentDO()
            userTorrentDO.infoHash = torrentDO.infoHash
            userTorrentDO.userId = user.id
            userTorrentDO.createTime = LocalDateTime.now()
            userTorrentDO.authKey = IdUtil.fastSimpleUUID()
            userTorrentDO.status = 0
            database.userTorrentDO.add(userTorrentDO)
        }

        //构建下载者的announce
        val announceUrl = setting["pt.announce.url"]
        torrentDO.announce = "${announceUrl}?auth_key=${userTorrentDO.authKey}"

        val info = database.from(TorrentInfo).select(TorrentInfo.infoHash, TorrentInfo.info)
            .where { TorrentInfo.infoHash eq torrentDO.infoHash }.limit(1)
            .map { it.getBytes(2) }
            .last()

        //返回
        call.respondBytes(
            bytes = toBeMap(torrentDO, info!!),
            contentType = ContentType.parse("application/x-bittorrent")
        )
    }
}


fun Route.auth() {

    /**
     * 上传文件
     */
    post("/upload/torrent") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user!!
        val multipartData = call.receiveMultipart()
        val reqMap = receiveFrom(multipartData)

        val toTorrent = toTorrent(reqMap["torrent"] as Map<String,Any>)
        val torrent = toTorrent.first
        torrent.userId = user.id
        torrent.status = 0
        torrent.uploadTime = LocalDateTime.now()
        torrent.type = reqMap["type"] as String
        torrent.labels = gson.fromJson<List<String>>(reqMap["labels"] as String, List::class.java)
        torrent.title = reqMap["title"] as String

        val torrentInfoDO = InfoDO()
        torrentInfoDO.info = toTorrent.second
        torrentInfoDO.description = reqMap["description"] as String
        torrentInfoDO.infoHash = torrent.infoHash

        //查询是否已经存在
        val existTorrentDO = database.torrentDO.filter { it.infoHash eq torrent.infoHash }
            .firstOrNull()
        if (existTorrentDO != null) {
            call.respond(Result(40000, "不要上传重复的文件"))
            return@post
        }

        database.useTransaction {
            try {
                database.torrentInfoDO.add(torrentInfoDO)
                database.torrentDO.add(torrent)
                it.commit()
            } catch (e: Exception) {
                it.rollback()
                logger.error("", e)
            }
        }
        call.respond(Result())
    }




}

suspend fun receiveFrom(multipartData:MultiPartData):Map<String, Any>{
    val reqMap = hashMapOf<String, Any>()
    multipartData.forEachPart { partData ->
        when (partData) {
            is PartData.FileItem -> {
                reqMap["torrent"]= BencodeInputStream(partData.streamProvider(), charset("utf8"),true).readDictionary()
            }
            is PartData.FormItem -> {
                reqMap[partData.name!!] = partData.value
            }
            else -> {

            }
        }
    }
    return  reqMap
}