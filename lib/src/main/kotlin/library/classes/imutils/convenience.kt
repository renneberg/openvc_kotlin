package library.classes.imutils

import library.classes.Size
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.lang.Math.max
import java.lang.Math.min
import java.net.URL
import kotlin.math.absoluteValue

fun translate(image: Mat, x: Double, y: Double): Mat {
    // define the translation matrix and perform the translation
    val M = Mat(2, 3, image.type())
    M.put(0, 0, 1.0)
    M.put(0, 1, 0.0)
    M.put(0, 2, x)
    M.put(1, 0, 0.0)
    M.put(1, 1, 1.0)
    M.put(1, 2, y)

    val shifted = Mat()
    Imgproc.warpAffine(image, shifted, M, image.size(), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT)

    // return the translated image
    return shifted
}

fun rotate(image: Mat, angle: Double, center: Point? = null, scale: Double = 1.0): Mat {
    // Grab the dimensions of the image
    val (h, w) = image.size()

    // If the center is null, initialize it as the center of the image
    val c = center ?: Point(w / 2, h / 2)

    // Perform the rotation
    val M = Imgproc.getRotationMatrix2D(c, angle, scale)
    val rotated = Mat()
    Imgproc.warpAffine(image, rotated, M, Size(w, h), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, Scalar(0.0, 0.0, 0.0))

    // Return the rotated image
    return rotated
}

fun rotateBound(image: Mat, angle: Double): Mat {
    // Grab the dimensions of the image and then determine the center
    val (h, w) = image.size()
    val cX = w / 2
    val cY = h / 2

    // Grab the rotation matrix (applying the negative of the angle
    // to rotate clockwise), then grab the sine and cosine
    // (i.e., the rotation components of the matrix)
    val M = Imgproc.getRotationMatrix2D(Point(cX, cY), -angle, 1.0)

    val cos = kotlin.math.abs(M.get(0, 0)[0].toFloat())
    val sin = kotlin.math.abs(M.get(0, 1)[0].toFloat())

    // Compute the new bounding dimensions of the image
    val nW = (h * sin + w * cos).toInt()
    val nH = (h * cos + w * sin).toInt()

    // Adjust the rotation matrix to take into account translation
    M.put(0, 2, M.get(0, 2)[0] + (nW / 2) - cX)
    M.put(1, 2, M.get(1, 2)[0] + (nH / 2) - cY)

    // Perform the actual rotation and return the image
    val rotated = Mat()
    Imgproc.warpAffine(image, rotated, M, Size(nW, nH), Imgproc.INTER_LINEAR, Core.BORDER_CONSTANT, Scalar(0.0, 0.0, 0.0))
    return rotated
}

fun resize(image: Mat, width: Int? = null, height: Int? = null, inter: Int = Imgproc.INTER_AREA): Mat {
    // initialize the dimensions of the image to be resized and grab the image size
    var dim: Size? = null
    val (h, w) = image.size()

    // if both the width and height are None, then return the original image
    if (width == null && height == null) {
        return image
    }

    // check to see if the width is None
    if (width == null) {
        // calculate the ratio of the height and construct the dimensions
        val r = height!!.toDouble() / h
        dim = Size((w * r).toInt(), height)

        // otherwise, the height is None
    } else {
        // calculate the ratio of the width and construct the dimensions
        val r = width.toDouble() / w
        dim = Size(width, (h * r).toInt())
    }

    // resize the image
    val resized = Mat()
    Imgproc.resize(image, resized, dim, 0.0, 0.0, inter)

    // return the resized image
    return resized
}

fun skeletonize(image: Mat, size: Size, structuring: Int = Imgproc.MORPH_RECT): Mat {
    // Determine the area (i.e. total number of pixels in the image),
    // initialize the output skeletonized image, and construct the
    // morphological structuring element.
    val area = image.rows() * image.cols()
    val skeleton = Mat.zeros(image.size(), CvType.CV_8UC1)
    val elem = Imgproc.getStructuringElement(structuring, size)

    // Keep looping until the erosions remove all pixels from the
    // image.
    while (true) {
        // Erode and dilate the image using the structuring element.
        val eroded = Mat()
        Imgproc.erode(image, eroded, elem)
        val temp = Mat()
        Imgproc.dilate(eroded, temp, elem)

        // Subtract the temporary image from the original, eroded
        // image, then take the bitwise 'or' between the skeleton
        // and the temporary image.
        Core.subtract(image, temp, temp)
        Core.bitwise_or(skeleton, temp, skeleton)
        eroded.copyTo(image)

        // If there are no more 'white' pixels in the image, then
        // break from the loop.
        if (area == area - Core.countNonZero(image)) {
            break
        }
    }

    // Return the skeletonized image.
    return skeleton
}

fun opencv2matplotlib(image: Mat): Mat {
    val mat = Mat()
    Imgproc.cvtColor(image, mat, Imgproc.COLOR_BGR2RGB)
    return mat
}

fun urlToImage(url: String, readFlag: Int = Imgcodecs.IMREAD_COLOR): Mat {
    val urlObj = URL(url)
    val inputStream = urlObj.openStream()
    val byteArray = ByteArrayOutputStream()
    var byte = inputStream.read()
    while (byte != -1) {
        byteArray.write(byte)
        byte = inputStream.read()
    }
    val imageArray = byteArray.toByteArray()
    val mat = Imgcodecs.imdecode(MatOfByte(*imageArray), readFlag)
    return mat
}

fun autoCanny(image: Mat, sigma: Double = 0.33): Mat {
    val mat = Mat()
    Imgproc.medianBlur(image, mat, 3)
    val v = Core.mean(mat).`val`[0]
    val lower = 0.0.coerceAtLeast((1.0 - sigma) * v)
    val upper = 255.0.coerceAtMost((1.0 + sigma) * v)
    Imgproc.Canny(image, mat, lower, upper)
    return mat
}

fun openCV2Matplotlib(image: Mat): Mat {
    // OpenCV represents images in BGR order; however, Matplotlib expects the image in RGB order, so simply convert from BGR to RGB and return
    val rgbImage = Mat()
    Imgproc.cvtColor(image, rgbImage, Imgproc.COLOR_BGR2RGB)
    return rgbImage
}

