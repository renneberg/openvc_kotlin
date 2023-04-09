package library.classes.imutils

import org.opencv.core.*
import java.util.regex.Pattern


fun findFunction(name: String, prettyPrint: Boolean = true, module: Any? = null): List<String> {
    // If the module is null, initialize it to the root `opencv` library
    val m = module ?: Core()

    // Grab all function names that contain `name` from the module
    val p = Pattern.compile(".*$name.*", Pattern.CASE_INSENSITIVE)
    val filtered = m.javaClass.methods
        .filter { p.matcher(it.name).matches() }
        .map { it.name }

    // Check to see if the filtered names should be returned to the calling function
    if (!prettyPrint) {
        return filtered
    }

    // Otherwise, loop over the function names and print them
    for ((i, funcName) in filtered.withIndex()) {
        println("${i + 1}. $funcName")
    }

    return emptyList()
}