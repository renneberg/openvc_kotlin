package library.converters

import library.classes.ContourAreaH
import library.classes.Point
import library.classes.points
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

fun  IntArray.toMatOfInt(): MatOfInt {
    val matOfInt = MatOfInt()
    matOfInt.fromList(this.asList())
    return matOfInt
}

fun MatOfPoint2f.toMatOfPoint( ): MatOfPoint {
    val pointList = this.toList().map { Point(it.x, it.y) }
    return MatOfPoint(*pointList.toTypedArray())
}

fun MatOfPoint2f.toListPoint(): List<Point> {

    val points: List<Point> = this.toList().map { point2f ->
        Point(point2f.x.toInt(), point2f.y.toInt())
    }
    return points
}

@Throws(java.lang.Exception::class)
fun Mat.Mat2BufferedImage(): BufferedImage {
    val mob = MatOfByte()
    Imgcodecs.imencode(".jpg", this, mob)
    val ba = mob.toArray()
    return ImageIO.read(ByteArrayInputStream(ba))
}



fun MatOfPoint2f.toListOfMatOfPoint(): List<MatOfPoint> {
    val result = mutableListOf<MatOfPoint>()
    val matOfPoint = MatOfPoint()
    this.convertTo(matOfPoint, CvType.CV_32S)
    result.add(matOfPoint)
    return result
}

fun MatOfPoint.toListOfMatOfPoint(): List<MatOfPoint> {
   return this.toMatOfPoint2f().toListOfMatOfPoint()
}

fun MatOfPoint.toMatOfPoint2f(): MatOfPoint2f {
   return MatOfPoint2f(*this.toArray())
}

fun MatOfPoint.toBoundingRect(): Rect {
   return Imgproc.minAreaRect(this.toMatOfPoint2f()).boundingRect()
}
fun MatOfPoint.toRotatedRect(): RotatedRect {
    return Imgproc.minAreaRect(this.toMatOfPoint2f())
}

fun Array<Point>.toRotatedRect(): MatOfPoint {
    val mat = MatOfPoint().apply {
        fromArray(*this@toRotatedRect)
    }

    return mat
}
fun RotatedRect.toRotatedRect(): MatOfPoint {
    val mat = MatOfPoint().apply {
        fromArray(*this@toRotatedRect.points())
    }
    return mat

}

fun grabContours(cnts: List<MatOfPoint>): List<MatOfPoint> {
    var contours = cnts
    if (cnts.size == 2) {
        contours = listOf(cnts[0])
    } else if (cnts.size == 3) {
        contours = listOf(cnts[1])
    } else {
        throw Exception("Contours tuple must have length 2 or 3, otherwise OpenCV changed their cv2.findContours return signature yet again. Refer to OpenCV's documentation in that case")
    }
    return contours
}

fun buildMontages(imageList: List<Mat>, imageShape: Pair<Int, Int>, montageShape: Pair<Int, Int>): List<Mat> {
    if (imageShape.first <= 0 || imageShape.second <= 0) {
        throw IllegalArgumentException("Image shape must be a pair of positive integers.")
    }
    if (montageShape.first <= 0 || montageShape.second <= 0) {
        throw IllegalArgumentException("Montage shape must be a pair of positive integers.")
    }
    val imageMontages = mutableListOf<Mat>()
    // Start with black canvas to draw images onto
    var montageImage = Mat.zeros(imageShape.second * montageShape.second, imageShape.first * montageShape.first, CvType.CV_8UC3)
    var cursorPos = Pair(0, 0)
    var startNewImg = false
    for (img in imageList) {
        if (img.type() != CvType.CV_8UC3) {
            throw IllegalArgumentException("Input must be a 3-channel BGR image.")
        }
        startNewImg = false
        val resizedImg = Mat()
        Imgproc.resize(img, resizedImg, Size(imageShape.first.toDouble(), imageShape.second.toDouble()))
        // Draw image to black canvas
        val roi = montageImage.submat(cursorPos.second, cursorPos.second + imageShape.second, cursorPos.first, cursorPos.first + imageShape.first)
        resizedImg.copyTo(roi)
        cursorPos = Pair(cursorPos.first + imageShape.first, cursorPos.second) // Increment cursor x position
        if (cursorPos.first >= montageShape.first * imageShape.first) {
            cursorPos = Pair(0, cursorPos.second + imageShape.second) // Increment cursor y position
            if (cursorPos.second >= montageShape.second * imageShape.second) {
                cursorPos = Pair(0, 0)
                imageMontages.add(montageImage.clone())
                // Reset black canvas
                montageImage = Mat.zeros(imageShape.second * montageShape.second, imageShape.first * montageShape.first, CvType.CV_8UC3)
                startNewImg = true
            }
        }
    }
    if (!startNewImg) {
        imageMontages.add(montageImage.clone()) // Add unfinished montage
    }
    return imageMontages
}

fun adjustBrightnessContrast(image: Mat, brightness: Double = 0.0, contrast: Double = 0.0): Mat {
    val beta = 0.0
    val adjustedImage = Mat()
    Core.addWeighted(image, 1.0 + contrast / 100.0, image, beta, brightness, adjustedImage)
    return adjustedImage
}

fun ArrayList<ContourAreaH>.toCountourList(): List<MatOfPoint> {
    val out = arrayListOf<MatOfPoint>()
    for (item in this)
    {
        out.add(item.matOfPoint)
    }
    return out.toList()
}

fun List<MatOfPoint>.toArrayList(): ArrayList<MatOfPoint> {
    val out = arrayListOf<MatOfPoint>()
    for (item in this)
    {
        out.add(item)
    }
    return out
}

fun ContourAreaH.toRect(): Rect {
    return this.rect.boundingRect()
}