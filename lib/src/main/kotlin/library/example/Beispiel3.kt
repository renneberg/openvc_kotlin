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
import org.opencv.videoio.VideoCapture

object Beispiel3 {
    val cv = cv2()
    val np = numpy()
    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        for (item in args) {
            print(item)
        }

        var cap = VideoCapture(0)



        val img =   imread("smarties.png")



        var callback =fun(x:Int){
/*            val l_h = cv.getTrackbarPos("LH")
            val l_s = cv.getTrackbarPos("LS")
            val l_v = cv.getTrackbarPos("LV")
            val u_h = cv.getTrackbarPos("UH")
            val u_s = cv.getTrackbarPos("US")
            val u_v = cv.getTrackbarPos("UV")

            var l_b = np.array(l_h,l_s,l_v)
            var u_b = np.array(u_h,u_s,u_v)


          //  var mask = hsv.inRange(l_b,u_b)
         //   val res = bitwise_and(frame,frame,mask)

            cv.addImage("bild1",  img)
            cv.addImage("bild2",  img)
            cv.addImage("bild3",  img)
            cv.addImage("bild4",  img)*/

        }

        cv.namedWindows("tracking")
        cv.createTrackbar("LH","tracking",0,255,callback)
        cv.createTrackbar("LS","tracking",0,255,callback)
        cv.createTrackbar("LV","tracking",0,255,callback)
        cv.createTrackbar("UH","tracking",0,255,255,callback)
        cv.createTrackbar("US","tracking",0,255,255,callback)
        cv.createTrackbar("UV","tracking",0,255,255,callback)
/*



        var l_b = np.array(0,0,0)
        val u_b = np.array(255,255,255)

         val mask = hsv.inRange(l_b,u_b)

        val b_and = bitwise_and(frame,frame,mask)

*/

        cv.addImage("bild1", "tracking", img)
        cv.addImage("bild2", "imgage1", img)
        cv.addImage("bild3", "imgage2", img)
        cv.addImage("bild4", "imgage3", img)
        cv.imshow()

        while (true){
            val (_,frame) = cap.read()

            daten(frame)
        }

    }


    fun daten(img : Mat)
    {
        val hsv = img.cvtColor(COLOR_BGR2HSV)
        val l_h = cv.getTrackbarPos("LH")
        val l_s = cv.getTrackbarPos("LS")
        val l_v = cv.getTrackbarPos("LV")
        val u_h = cv.getTrackbarPos("UH")
        val u_s = cv.getTrackbarPos("US")
        val u_v = cv.getTrackbarPos("UV")

        val l_b = np.array(l_h,l_s,l_v)
        val u_b = np.array(u_h,u_s,u_v)


        val mask = hsv.inRange(l_b,u_b)
        val res = bitwise_and(img,img,mask)

        cv.addImage("bild1",  hsv)
        cv.addImage("bild2",  mask)
        cv.addImage("bild3",  res)
        cv.addImage("bild4",  img)

    }

}


