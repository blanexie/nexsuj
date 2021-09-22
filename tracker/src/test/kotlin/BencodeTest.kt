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
        val beObj = benDecode(wrap)
        assert(beObj is BeInt)
        assert(beObj.toBenStr() == value)
        assert(beObj.getValue() == value.substring(1, value.length - 1).toLong())
    }

    @Test
    fun beStr() {
        val value = "10:i19safag8e"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val beObj = benDecode(wrap)
        assert(beObj is BeStr)
        assert(beObj.toBenStr() == value)
        assert(beObj.getValue() == value.substring(3, value.length))
    }


    @Test
    fun beList() {
        val value = "l10:i19safag8ei13ee"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val pair = benDecode(wrap)
        assert(pair is BeList)

        val list = (pair as BeList).getValue()
        val toStr = pair.toBenStr()
        assert(toStr == value)

        assert(wrap.position() == 19)

        assert(list.size == 2)
        assert(list[0] is BeStr)
        assert(list[1] is BeInt)

    }

    @Test
    fun beMap() {
        val value = "d3:key10:i19safag8e2:vai13ee"
        val wrap = ByteBuffer.wrap(value.toByteArray())
        val pair = benDecode(wrap)
        assert(pair is BeMap)

        val map = (pair as BeMap).getValue()
        val toStr = pair.toBenStr()
        assert(toStr == value)
        assert(map.size == 2)

        assert(wrap.position() == 28)

        var flag = true
        for (entry in map) {
            if (flag) {
                assert(entry.value is BeStr)
                flag = false
            } else {
                assert(entry.value is BeInt)
            }
        }

    }


    @Test
    fun beTorrent() {
        val classPathResource = ClassPathResource("mutableFiles.torrent")
        val readBytes = classPathResource.readBytes()
        val wrap = ByteBuffer.wrap(readBytes)
        val benDecode = benDecode(wrap)
        assert(wrap.position() == readBytes.size)

        val torrent = Torrent.build(benDecode as BeMap)

        val toBeMap = torrent.toBeMap()
        println(toBeMap)

    }


    @Test
    fun beTorrent2() {
        val classPathResource = ClassPathResource("signleFile.torrent")
        val readBytes = classPathResource.readBytes()
        val wrap = ByteBuffer.wrap(readBytes)
        val benDecode = benDecode(wrap)
        assert(wrap.position() == readBytes.size)
        val torrent = Torrent.build(benDecode as BeMap)
        println(JSONUtil.toJsonStr(torrent))

        val toBeMap = torrent.toBeMap()

        println(JSONUtil.toJsonStr(toBeMap))




    }


}