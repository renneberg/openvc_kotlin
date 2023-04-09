package library.example

import library.classes.Size
import library.classes.cv2
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import org.opencv.imgcodecs.Imgcodecs.imread
import java.io.File
import java.nio.FloatBuffer

object Main  {
    val cv = cv2()
    val dir = File("/home/zermos/Desktop/Orginal/4/80001/").listFiles()

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        for (item in args) {
            print(item)
        }



        val CATEGORIES = listOf("citycards_normal","citycards_draht")

        fun prepare(file: String): Mat {
            val IMG_SIZE = 50
            val img = imread(file, IMREAD_GRAYSCALE)
            val resizedImg = Mat()
            val size = Size(100, 220)
            org.opencv.imgproc.Imgproc.resize(img, resizedImg, size)
            val floatBuffer = FloatBuffer.allocate(IMG_SIZE * IMG_SIZE)
            resizedImg.convertTo(resizedImg, CvType.CV_32F, 1.0 / 255.0)
            resizedImg.get(0, 0, floatBuffer.array())
            val mat = Mat(IMG_SIZE, IMG_SIZE, CvType.CV_32F)
            mat.put(0, 0, floatBuffer.array())


            val xName = "inputs_tensor_info_name"
            val yName = "outputs_tensor_info_name"

            return  mat

        }
    //    val model = SavedModelBundle.load("model.h5", "serve")
       // val model = SavedModelBundle.load("CNN.model/", "serve")



    }
}


