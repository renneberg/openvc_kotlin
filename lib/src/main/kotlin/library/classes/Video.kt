package library.classes

import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture





fun VideoCapture.read(): Pair<Boolean, Mat> {
    val m = Mat()
    this.read(m)
    return Pair(this.isOpened, m)
}