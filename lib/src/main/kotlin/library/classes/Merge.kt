package library.classes

import org.opencv.core.Core
import org.opencv.core.Mat

fun merge(channels : List<Mat>) {
    val img = Mat()
    // Merge the channels back into an image
    Core.merge(channels, img)
}



