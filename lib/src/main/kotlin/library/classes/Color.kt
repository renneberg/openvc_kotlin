package library.classes

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


@JvmName("cvtColor1")
fun cvtColor(src: Mat, code: Int): Mat {
    // Convert the image to grayscale

    val gray = try {
        val out = Mat()
        Imgproc.cvtColor(src, out, code)
        out
    } catch (e: Exception) {
        src
    }

    return gray
}


fun Mat.cvtColor( code: Int): Mat {
    // Convert the image to grayscale

    val gray = try {
        val out = Mat()
        Imgproc.cvtColor(this, out, code)
        out
    } catch (e: Exception) {
        this
    }

    return gray
}