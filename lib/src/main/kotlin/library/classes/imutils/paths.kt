package library.classes.imutils

import java.util.*
import java.io.File

val imageTypes = arrayOf(".jpg", ".jpeg", ".png", ".bmp", ".tif", ".tiff")

fun listImages(basePath: String, contains: String? = null): List<String> {
    return listFiles(basePath, validExts = imageTypes, contains = contains)
}

fun listFiles(basePath: String, validExts: Array<String>? = null, contains: String? = null): List<String> {
    val fileList = mutableListOf<String>()
    File(basePath).walkTopDown().forEach { file ->
        if (file.isFile) {
            // if the contains string is not null and the filename does not contain
            // the supplied string, then ignore the file
            if (contains != null && !file.name.contains(contains)) {
                return@forEach
            }

            // determine the file extension of the current file
            val ext = file.extension.lowercase(Locale.getDefault())

            // check to see if the file is an image and should be processed
            if (validExts == null || ext in validExts) {
                // add the path to the file to the list
                fileList.add(file.path)
            }
        }
    }
    return fileList
}