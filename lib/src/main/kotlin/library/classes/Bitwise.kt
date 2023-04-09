package library.classes

import org.opencv.core.Core
import org.opencv.core.Mat

fun bitwise_or(src1: Mat, src2: Mat): Mat {
    val dst = Mat()
    Core.bitwise_or(src1, src2, dst)
    return dst
}

//
// C++:  void cv::bitwise_or(Mat src1, Mat src2, Mat& dst, Mat mask = Mat())
//
fun bitwise_or(src1: Mat, src2: Mat, mask: Mat): Mat {
    val dst = Mat()
    Core.bitwise_or(src1, src2, dst, mask)
    return dst
}

fun bitwise_and(src1: Mat, src2: Mat): Mat {
    val dst = Mat()
    Core.bitwise_and(src1, src2, dst)
    return dst
}

//
// C++:  void cv::bitwise_and(Mat src1, Mat src2, Mat& dst, Mat mask = Mat())
//
fun bitwise_and(src1: Mat, src2: Mat, mask: Mat): Mat {
    val dst = Mat()
    Core.bitwise_and(src1, src2, dst, mask)
    return dst
}