package com.github.blanexie.nexusj.controller

import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.URLUtil
import com.dampcake.bencode.BencodeInputStream
import com.github.blanexie.nexusj.bencode.toTorrent
import com.github.blanexie.nexusj.controller.param.Result
import com.github.blanexie.nexusj.controller.param.TorrentQuery
import com.github.blanexie.nexusj.controller.param.UserQuery
import com.github.blanexie.nexusj.support.*
import com.github.blanexie.nexusj.table.RoleDO
import com.github.blanexie.nexusj.table.TorrentDO
import com.github.blanexie.nexusj.table.UserDO
import com.github.blanexie.nexusj.table.UserTorrentDO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
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

        val userDO =  UserDO.findByEmailAndPwd(loginParam.email!!,loginParam.pwd!!)
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

    /**
     * 搜索查询
     */
    post("/torrent/list") {
        val principal = call.authentication.principal<UserPrincipal>()!!
        val user = principal.user

        val torrentQuery = call.receive<TorrentQuery>()
        val result = TorrentDO.findByQuery(torrentQuery, user.id)
        val ids = result.map { it.userId }.toList()
        val userDOs = UserDO.findByIds(ids)
        val map1 = result.map { torrent ->
            val first = userDOs.first { it.id == torrent.userId }
            val map = mutableMapOf<String, Any?>()
            torrent.properties.forEach { (t, u) -> map[t] = u }
            map["uploadUserName"] = first.nick
            map
        }
        call.respond(Result(body = mapOf("result" to map1)))
        return@post
    }

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
        if (reqMap.containsKey("result")) {
            call.respond(reqMap["result"]!!)
            return@post
        }
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
                val name = partData.name
                if (name == "torrent") {
                    reqMap[name] =
                        BencodeInputStream(partData.streamProvider(), charset("utf8"), true).readDictionary()
                } else {
                    val originalFileName = partData.originalFileName
                    //限制， 只接受特定后缀的文件
                    if (originalFileName!!.endsWith(".jpg", true)
                        || originalFileName.endsWith(".jpeg", true)
                        || originalFileName.endsWith(".png", true)

                    ) {
                        val lastIndexOf = originalFileName.lastIndexOf(".")
                        val suffix = originalFileName.substring(lastIndexOf)
                        //限定 只接受特定参数名称的文件
                        if (name == "imgList") {
                            //修改文件名称，防止
                            val fileName = "${IdUtil.fastSimpleUUID()}$suffix"
                            val file = File("$tempDir/$fileName")
                            file.outputStream().use { out ->
                                IoUtil.copy(partData.streamProvider(), out)
                            }
                            val imgList = reqMap["imgList"] ?: arrayListOf<String>()
                            val arrayList = imgList as ArrayList<String>
                            arrayList.add(fileName)
                            reqMap["imgList"] = arrayList
                        }
                        //限定 只接受特定参数名称的文件
                        if (name == "coverPath") {
                            val fileName = "${IdUtil.fastSimpleUUID()}$suffix"
                            val file = File("$tempDir/$fileName")
                            file.outputStream().use { out ->
                                IoUtil.copy(partData.streamProvider(), out)
                            }
                            reqMap["coverPath"] = fileName
                        }

                    } else {
                        reqMap.clear()
                        reqMap["result"] = Result.BadFileUpload
                        return@forEachPart
                    }
                }
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
