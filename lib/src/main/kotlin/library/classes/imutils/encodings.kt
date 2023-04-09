package library.classes.imutils

import org.json.JSONObject
import java.util.*

fun base64EncodeImage(a: ByteArray, dtype: String, shape: List<Int>): String {
    val encodedArray = Base64.getEncoder().encodeToString(a)
    val jsonObj = JSONObject()
    jsonObj.put("data", encodedArray)
    jsonObj.put("dtype", dtype)
    jsonObj.put("shape", shape)
    return jsonObj.toString()
}

fun base64DecodeImage(jsonStr: String): ByteArray {
    val jsonObj = JSONObject(jsonStr)
    val encodedArray = jsonObj.getString("data")
    return Base64.getDecoder().decode(encodedArray)
}