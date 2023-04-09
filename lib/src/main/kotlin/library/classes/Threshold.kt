package library.classes

import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

fun threshold(src : Mat, thresh : Int, maxval : Int, type : Int): Mat {
    return threshold(src, thresh.toDouble(), maxval.toDouble(), type)
}

@JvmName("threshold2")
fun Mat.threshold( thresh : Int, maxval : Int, type : Int): Mat {
    return threshold(this, thresh.toDouble(), maxval.toDouble(), type)
}

fun threshold(src : Mat, thresh : Double, maxval : Double, type : Int): Mat {
    val dst = Mat()
    Imgproc.threshold(src, dst, thresh, maxval, type)
    return dst
}


@JvmName("adaptiveThreshold2")
fun Mat.adaptiveThreshold( maxValue : Int, adaptiveMethod : Int, thresholdType : Int, blockSize : Int, C : Int): Mat {
    return adaptiveThreshold(this,maxValue.toDouble(),adaptiveMethod,thresholdType,blockSize,C.toDouble())
}


fun adaptiveThreshold(src : Mat, maxValue : Int, adaptiveMethod : Int, thresholdType : Int, blockSize : Int, C : Int): Mat {
    return adaptiveThreshold(src,maxValue.toDouble(),adaptiveMethod,thresholdType,blockSize,C.toDouble())
}

fun adaptiveThreshold(src : Mat, maxValue : Double, adaptiveMethod : Int, thresholdType : Int, blockSize : Int, C : Double): Mat {
    val dst = Mat()
    Imgproc.adaptiveThreshold(src,dst,maxValue,adaptiveMethod,thresholdType,blockSize,C)
    return dst
}

