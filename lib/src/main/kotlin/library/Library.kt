/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package library

import library.classes.cv2
import org.opencv.core.Core

class Library {
    fun someLibraryMethod(): Boolean {
print(Core.NATIVE_LIBRARY_NAME)

        return true
    }


}

/*
fun main(args: Array<String>) {
for (item in args)
    print(item)
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    var cv = cv2()
    cv.namedWindows("heute")
    cv.imshow()
}*/
