package library.classes

import org.opencv.core.Core
import org.opencv.core.Mat


fun Mat.split(): MutableList<Mat> {
// Split the image into its BGR channels
    val channels = mutableListOf<Mat>()
    Core.split(this, channels)
    return channels
}


