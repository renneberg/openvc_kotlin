package library.classes

import library.converters.toMatOfPoint2f
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.imgproc.Imgproc

fun detect(c: MatOfPoint): String {
    var shape = "unidentified"
    val peri = Imgproc.arcLength(c.toMatOfPoint2f(), true)
    val approx = MatOfPoint2f()
    Imgproc.approxPolyDP(MatOfPoint2f(*c.toArray()), approx, 0.04 * peri, true)
    when (approx.toList().size) {
        3 -> shape = "triangle"
        4 -> {
            val rect = Imgproc.boundingRect(approx)
            val ar = rect.width.toDouble() / rect.height.toDouble()
            shape = if (ar >= 0.95 && ar <= 1.05) "square" else "rectangle"
        }
        5 -> shape = "pentagon"
        else -> shape = "circle"
    }
    return shape
}