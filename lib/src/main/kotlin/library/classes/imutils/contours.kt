package library.classes.imutils

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.FONT_HERSHEY_SIMPLEX

fun sortContours(cnts: List<MatOfPoint>, method: String = "left-to-right"): Pair<List<MatOfPoint>, List<Rect>> {
    // initialize the reverse flag and sort index
    var reverse = false
    var i = 0

    // handle if we need to sort in reverse
    if (method == "right-to-left" || method == "bottom-to-top") {
        reverse = true
    }

    // handle if we are sorting against the y-coordinate rather than
    // the x-coordinate of the bounding box

    i  = if (method == "top-to-bottom" || method == "bottom-to-top") 1 else 0



    // construct the list of bounding boxes and sort them from top to
    // bottom
    val boundingBoxes = cnts.map { Imgproc.boundingRect(it) }
    val zipped = cnts.zip(boundingBoxes)
    val sorted = zipped.sortedBy {

        when (i)
        {
            1 ->  it.second.y
            else -> it.second.x
        }

    }

    val (sortedCnts, sortedBoxes) = sorted.unzip()
    return Pair(sortedCnts, sortedBoxes)
}


fun labelContour(image: Mat, c: MatOfPoint, i: Int, color: Scalar = Scalar(0.0, 255.0, 0.0), thickness: Int = 2): Mat {
    // compute the center of the contour area and draw a circle
    // representing the center
    val M = Imgproc.moments(c)
    val cX = (M.m10 / M.m00).toInt()
    val cY = (M.m01 / M.m00).toInt()
    val center = Point(cX.toDouble(), cY.toDouble())

    // draw the contour and label number on the image
    Imgproc.drawContours(image, listOf(c), -1, color, thickness)
    Imgproc.putText(image, "#${i+1}", Point(cX-20.0, cY.toDouble()), FONT_HERSHEY_SIMPLEX,
        1.0, Scalar(255.0, 255.0, 255.0), 2)

    // return the image with the contour number drawn on it
    return image
}

@JvmName("labelContour2")
fun Mat.labelContour( c: MatOfPoint, i: Int, color: Scalar = Scalar(0.0, 255.0, 0.0), thickness: Int = 2): Mat {
    // compute the center of the contour area and draw a circle
    // representing the center
    val M = Imgproc.moments(c)
    val cX = (M.m10 / M.m00).toInt()
    val cY = (M.m01 / M.m00).toInt()
    val center = Point(cX.toDouble(), cY.toDouble())

    // draw the contour and label number on the image
    Imgproc.drawContours(this, listOf(c), -1, color, thickness)
    Imgproc.putText(this, "#${i+1}", Point(cX-20.0, cY.toDouble()), FONT_HERSHEY_SIMPLEX,
        1.0, Scalar(255.0, 255.0, 255.0), 2)

    // return the image with the contour number drawn on it
    return this
}