package library.classes

import library.Main
import library.converters.toListPoint
import library.converters.toMatOfInt
import library.converters.toMatOfPoint
import library.converters.toMatOfPoint2f
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.abs

class Find_Contours(val srt : Mat) {
    val squares: ArrayList<MatOfPoint> = arrayListOf()
    val contours: List<MatOfPoint> = ArrayList()
    val blurred =  srt.medianBlur(9)
    val gray0 = Mat(blurred.size(), CvType.CV_8U)


    fun start(){

    }


    fun findContours(){






    }













    fun Mat.withthreshold1(l:Int,thresholdLevel : Int): Mat {
        val gray = Mat()
        this.copyTo(gray)
        Imgproc.threshold(gray, gray, (l+1) * 255 / thresholdLevel.toDouble(), 255.0, Imgproc.THRESH_BINARY)
        return gray
    }

    fun Mat.withthreshold2(): Mat {
        val th3 = adaptiveThreshold(this,255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,199,15) .apply {dilate(this,
            Main.kernel,2)  }
        return th3
    }

    fun Mat.withCanny(): Mat {
        // Use Canny instead of zero threshold level!
        // Canny helps to catch squares with gradient shading
        val gray = Mat()
        Imgproc.Canny(this, gray, 10.0, 20.0, 3)
        // Dilate helps to remove potential holes between edge segments
        Imgproc.dilate(gray, gray, Mat(), Point(-1,-1))
        return gray
    }
}


fun Mat.findSquares1(removeShadow : Boolean = true): ArrayList<MatOfPoint>{
    val fC = Find_Contours(this)

    val Anzahl_der_durchlaeufe = 3

    return arrayListOf()
}




