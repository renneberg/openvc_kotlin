package library.example

import classes.numpy
import library.classes.Scalar
import library.classes.Size
import library.classes.cv2
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import java.awt.BorderLayout

object Beispiel1 {
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


        cv.namedWindows("heute")

        val img = ImFarbe(0, 0, 0)

        val callback = fun(_: Int) {
            val r = cv.getTrackbarPos("R")
            val g = cv.getTrackbarPos("G")
            val b = cv.getTrackbarPos("B")
            cv.addImage("bild1", ImFarbe(r, g, b))
        }


        cv.createTrackbar("R", "heute", 0, 255, callback)
        cv.createTrackbar("G", "heute", 0, 255, callback)
        cv.createTrackbar("B", "heute", 0, 255, callback)
        cv.addImage("bild1", "heute", img)
        cv.imshow()


    }

    fun ImFarbe(b: Int, g: Int, r: Int): Mat {

        val img = np.zeros(Size(200, 200), CvType.CV_8UC3).apply {
            this.setTo(Scalar(b, g, r))
        }
        return img
    }

}
