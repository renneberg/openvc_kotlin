package library.example

import classes.numpy
import library.classes.Scalar
import library.classes.Size
import library.classes.cv2
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import java.awt.BorderLayout
import library.classes.*
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*

object Beispiel2 {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }
    val cv = cv2()
    val np = numpy()

    @JvmStatic
    fun main(args: Array<String>) {
        for (item in args) {
            print(item)
        }


        cv.namedWindows("imgage")

        val img =   imread("data/lena.jpg")

        val switch = "color/gray"
        val font = FONT_HERSHEY_SIMPLEX


        val callback = fun(x: Int) {
            val r = cv.getTrackbarPos("CP")
            val g = cv.getTrackbarPos(switch)


           val out = if (g==0)
               img.clone()
            else
               img.clone().cvtColor(COLOR_BGR2GRAY)

            putText(out,r.toString(),Point(100,100),font, 4.0, Scalar(0,0,255),5)

            cv.addImage("bild1",out)
        }




        cv.createTrackbar("CP", "imgage", 10, 400, callback)
        cv.createTrackbar(switch, "imgage", 0, 1, callback)



        cv.addImage("bild1", "imgage", img)
        cv.imshow()


    }



}
