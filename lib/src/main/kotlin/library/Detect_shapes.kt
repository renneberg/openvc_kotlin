package library

import nu.pattern.OpenCV
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

fun main() {

    OpenCV.loadShared()
    // load the image
    val filename = "data/pic3.png"
    val image = Imgcodecs.imread(filename)

    // convert the image to grayscale and blur it slightly
    val gray = Mat()
    Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(gray, gray, Size(5.0, 5.0), 0.0)

    // threshold the image
    val thresh = Mat()
    Imgproc.threshold(gray, thresh, 60.0, 255.0, Imgproc.THRESH_BINARY)

    // find contours in the thresholded image
    val contours = mutableListOf<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)

    // loop over the contours
    for (i in contours.indices) {
        // approximate the contour
        val approx = MatOfPoint2f()
        val contour = contours[i]
        val peri = Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
        Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, 0.04 * peri, true)

        // classify the shape based on the number of vertices
        when (approx.toList().size) {
            3 -> {
                println("Triangle")
                Imgproc.drawContours(image, contours, i, Scalar(0.0, 255.0, 0.0), 2)
            }
            4 -> {
                val rect = Imgproc.boundingRect(contour)
                val aspectRatio = rect.width.toDouble() / rect.height
                if (aspectRatio in 0.95..1.05) {
                    println("Square")
                    Imgproc.drawContours(image, contours, i, Scalar(0.0, 0.0, 255.0), 2)
                } else {
                    println("Rectangle")
                    Imgproc.drawContours(image, contours, i, Scalar(0.0, 255.0, 255.0), 2)
                }
            }
            5 -> {
                println("Pentagon")
                Imgproc.drawContours(image, contours, i, Scalar(255.0, 0.0, 255.0), 2)
            }
            else -> {
                println("Circle")
                Imgproc.drawContours(image, contours, i, Scalar(255.0, 255.0, 0.0), 2)
            }
        }
    }

    // display the image
    Imgcodecs.imwrite("output.png", image)
    println("Output image written to file")
}
