package library.classes.imutils

import library.classes.dist
import library.classes.findMinValue
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.core.Core.NORM_L2
import kotlin.math.pow


fun orderPoints(pts: MatOfPoint2f): MatOfPoint2f {
    val xSorted = pts.toArray().sortedBy { it.x }.toTypedArray()
    val leftMost = xSorted.sliceArray(0..1).sortedBy { it.y }.toTypedArray()
    val rightMost = xSorted.sliceArray(2..3).sortedBy { it.y }.toTypedArray()

    val tl = leftMost[0]
    val bl = leftMost[1]

    val distMat = Mat(1, rightMost.size, CvType.CV_32FC2)
    for (i in rightMost.indices) {

        distMat.put(0, i, floatArrayOf(
            ((tl.x - rightMost[i].x).pow(2.0) + (tl.y - rightMost[i].y).pow(2.0)).toFloat(),
            i.toDouble().toFloat()
        )
        )

    }

    val (brIndex, _) = distMat.findMinValue()


    val tr = rightMost[brIndex]
    val rect = MatOfPoint2f(tl, tr, bl, rightMost[(brIndex + 1) % 2])

    return rect
}

fun fourPointTransform(image: Mat, pts: MatOfPoint2f): Mat {
    val rect = orderPoints(pts)
    val (tl, tr, br, bl) = rect.toArray().toList()

    val widthA = dist(tl, bl)
    val widthB = dist(tr, bl)
    val maxWidth = widthA.coerceAtLeast(widthB)

    val heightA = dist(tr, br)
    val heightB = dist(tl, bl)
    val maxHeight = heightA.coerceAtLeast(heightB)

    val dst = MatOfPoint2f(
        Point(0.0, 0.0),
        Point((maxWidth - 1), 0.0),
        Point((maxWidth - 1), (maxHeight - 1)),
        Point(0.0, (maxHeight - 1))
    )

    val M = Imgproc.getPerspectiveTransform(rect, dst)
    val warped = Mat()
    Imgproc.warpPerspective(image, warped, M, Size(maxWidth, maxHeight))

    return warped
}