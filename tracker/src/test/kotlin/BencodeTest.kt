import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.json.JSONUtil
import com.github.blanexie.tracker.bencode.*
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer

class BencodeTest {


    @Test
    fun beInt() {
        val value = "i198e"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val beObj = toBeObj(wrap)
        assert(beObj.type == BeType.BeInt)
        assert(beObj.getValue() == value.substring(1, value.length - 1).toLong())
    }

    @Test
    fun beStr() {
        val value = "10:i19safag8e"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val beObj = toBeObj(wrap)
        assert(beObj.type == BeType.BeStr)
        assert(beObj.getValue() == value.substring(3, value.length))
    }


    @Test
    fun beList() {
        val value = "l10:i19safag8ei13ee"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val pair = toBeObj(wrap)
        assert(pair.type == BeType.BeList)

        val list = pair.getValue() as List<*>
        val toStr = pair.toBen()

        assert(wrap.position() == 19)

        assert(list.size == 2)
        assert(list[0] is String)
        assert(list[1] is Long)

    }

    @Test
    fun beMap() {
        val value = "d3:key10:i19safag8e2:vai13ee"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val pair = toBeObj(wrap)
        assert(pair.type == BeType.BeMap)

        val map = pair.getValue() as Map<String, Any>
        val toStr = pair.toBen()
        assert(map.size == 2)

        assert(wrap.position() == 28)

        var flag = true
        for (entry in map) {
            if (flag) {
                assert(entry.value is String)
                flag = false
            } else {
                assert(entry.value is Long)
            }
        }
    }


    @Test
    fun beTorrent() {
        val classPathResource = ClassPathResource("ccc.torrent")
        val readBytes = classPathResource.readBytes()
        val wrap = ByteBuffer.wrap(readBytes)
        val benDecode = toBeObj(wrap)
        assert(wrap.position() == readBytes.size)


    }


    @Test
    fun beTorrent2() {
        val classPathResource = ClassPathResource("signleFile.torrent")
        val readBytes = classPathResource.readBytes()
        val wrap = ByteBuffer.wrap(readBytes)
        val benDecode = toBeObj(wrap)
        assert(wrap.position() == readBytes.size)
        val torrent = toTorrent(benDecode)
        println(JSONUtil.toJsonStr(torrent))

        val toBeMap = toBeMap(torrent)

        println(JSONUtil.toJsonStr(toBeMap))

    }


}