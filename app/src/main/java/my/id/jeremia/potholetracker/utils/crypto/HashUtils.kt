package my.id.jeremia.potholetracker.utils.crypto


import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

fun hashSHA256(value:String) : String{
    return Hashing.sha256()
        .hashString(value, StandardCharsets.UTF_8)
        .toString();
}