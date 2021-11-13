import cn.hutool.core.io.resource.ClassPathResource
import com.dampcake.bencode.Bencode
import com.dampcake.bencode.Type
import com.github.blanexie.nexusj.bencode.toTorrent
import org.junit.Assert
import org.junit.jupiter.api.Test
import java.nio.charset.Charset


class BencodeTest {


    @Test
    fun beTorrent() {
        val bencode = Bencode(Charset.forName("utf8"),true)
        val classPathResource = ClassPathResource("aaa.torrent")
        val readBytes = classPathResource.readBytes()
        val torrentMap = bencode.decode(readBytes, Type.DICTIONARY)
        val linkedHashMap = torrentMap["info"] as LinkedHashMap<String, Any>
        val infoBytes = bencode.encode(linkedHashMap)
        println(String(infoBytes))
        val infoMap = bencode.decode(infoBytes, Type.DICTIONARY)


    }

    @Test
    fun beTorrent2() {
        val bencode = Bencode()
        val mapOf = mapOf("piece length" to 124215)
        val a = bencode.encode(mapOf)
        val decode = bencode.decode(a, Type.DICTIONARY)

        println(decode)
    }
}