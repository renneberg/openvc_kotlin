package library.classes

import library.Main
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Math.max
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt
import library.converters.*
import org.opencv.core.Core
import org.opencv.core.Point
import kotlin.collections.ArrayList


data class ContourAreaH(val matOfPoint: MatOfPoint, val area: Double, val rect: RotatedRect)

fun Size(width: Int, height: Int): Size {
    return Size(width.toDouble(), height.toDouble())
}

fun Scalar(v0: Int, v1: Int, v2: Int): Scalar {
    return Scalar(v0.toDouble(), v1.toDouble(), v2.toDouble())
}

fun Point(v0: Int, v1: Int): Point {
    return Point(v0.toDouble(), v1.toDouble())
}

//
// C++:  void cv::inRange(Mat src, Scalar lowerb, Scalar upperb, Mat& dst)
//
fun inRange(src: Mat, lowerb: Scalar, upperb: Scalar): Mask {
    val dst = Mask()
    if (src.height() > 0 && src.width() > 0)
         Core.inRange(src, lowerb, upperb, dst)
    return dst
}

@JvmName("inrage1")
fun Mat.inRange(lowerb: Scalar, upperb: Scalar): Mask {
    val dst = Mask()
    Core.inRange(this, lowerb, upperb, dst)
    return dst
}

class Mask : Mat() {
    fun isMask(): Boolean {
        return try {
            this.findNonZero()
            true
        } catch (e: Exception) {
            false
        }
    }
}

fun Mask.findNonZero(): Mat {
    val nonZero = Mat()
    Core.findNonZero(this, nonZero)
    return nonZero
}

fun Mat.inRangeProcent(mask: Mask): Int {
    val countOrginal = this.rows() * this.cols()
    val nonZeroMask = mask.findNonZero()
    val countMask = nonZeroMask.rows() * nonZeroMask.cols()
    val prozent = countMask.toDouble() * 100 / countOrginal
    return prozent.roundToInt()
}


fun Mat.isMask(): Boolean {
    return try {
        val nonZero = Mat()
        Core.findNonZero(this, nonZero)
        true
    } catch (e: Exception) {
        false
    }

}


fun getTime(): Long {
    val c1 = Calendar.getInstance()
    return c1.timeInMillis

}

fun Mat.resize(scale: Double = 0.25, interpolation: Int = INTER_AREA): Mat {
    val p = scale
    val new_width = this.width().toDouble() * p
    val new_height = this.height().toDouble() * p
    val result = Mat()
    Imgproc.resize(this, result, Size(new_width, new_height), 0.0, 0.0, interpolation)
    return result
}

fun dilate(srt: Mat, mask: Mat, iterations: Int): Mat {
    val dst = Mat()
    Imgproc.dilate(srt, dst, mask, Point(0, 0), iterations)
    return dst
}

fun dilate(srt: Mat, mask: Mat): Mat {
    val dst = Mat()
    Imgproc.dilate(srt, dst, mask)
    return dst
}

fun erode(srt: Mat, kernel: Mat, iterations: Int): Mat {
    val dst = Mat()
    Imgproc.erode(srt, dst, kernel, Point(0.0, 0.0), iterations)
    return dst
}


fun warpPerspective(objectImage: Mat, homography: Mat, size: Size): Mat {
    val objectBounds = Mat()
    Imgproc.warpPerspective(objectImage, objectBounds, homography, size)
    return objectBounds
}

fun Mat.findAllRectangles(): List<RotatedRect> {
    val contours = mutableListOf<MatOfPoint>() // create a list to store the contours
    val hierarchy = Mat() // create a Mat object to store the hierarchy of the contours
    Imgproc.findContours(
        this,
        contours,
        hierarchy,
        Imgproc.RETR_TREE,
        Imgproc.CHAIN_APPROX_SIMPLE
    ) // find the contours in the image
    val rectangles = mutableListOf<RotatedRect>() // create a list to store the rectangles
    for (contour in contours) { // iterate over the contours
        val rect = Imgproc.minAreaRect(contour.toMatOfPoint2f()) // find the minimum bounding rectangle of the contour
        rectangles.add(rect) // add the rectangle to the list
    }
    return rectangles // return the list of rectangles
}




fun drawRotatedRectangle(image: Mat, rect: RotatedRect, thickness: Int = 2 ,color : Scalar = Scalar(0.0, 255.0, 0.0)) {
    val boundingRect = rect.boundingRect() // get the minimum bounding rectangle of the rotated rectangle
    val x1 = boundingRect.x // get the x coordinate of the top-left corner of the rectangle
    val y1 = boundingRect.y // get the y coordinate of the top-left corner of the rectangle
    val x2 = boundingRect.x + boundingRect.width // get the x coordinate of the bottom-right corner of the rectangle
    val y2 = boundingRect.y + boundingRect.height // get the y coordinate of the bottom-right corner of the rectangle

    val w = image.width() * 0.9 // 90% von Image width
    val w_min = floor(image.width() * 0.06).toInt()
    val w_rechts = image.width() * 0.8 // 90% von Image width

    if (boundingRect.width < w_min || boundingRect.height < 12)
        return

    if (boundingRect.width > w)
        return

    if (x1 > w_rechts)
        return

    if (x1 < w_min)
        return

   // println("\n${boundingRect.height}   ${boundingRect.width}   wmin = ${w_min}")




    Imgproc.rectangle(
        image,
        Point(x1, y1),
        Point(x2, y2), color, thickness
    )

}


data class cout(
    var rect: RotatedRect,
    val inside: ArrayList<RotatedRect> = arrayListOf(),
    val overlap: ArrayList<RotatedRect> = arrayListOf()
)


fun ArrayList<cout>.sortRect(srt : Mat ,rect: RotatedRect ,minx : Double = 0.06, miny : Double = 0.1): ArrayList<cout> {
    val clone: ArrayList<cout> = this

    val searchRect = rect.boundingRect()
    val sx1 = searchRect.x // get the x coordinate of the top-left corner of the rectangle
    val sy1 = searchRect.y // get the y coordinate of the top-left corner of the rectangle
    val sx2 = searchRect.x + searchRect.width // get the x coordinate of the bottom-right corner of the rectangle
    val sy2 = searchRect.y + searchRect.height // get the y coordinate of the bottom-right corner of the rectangle

   val w_min =  if (minx <= 1)
                    floor(srt.width() * minx).toInt() // 90% von Image width
    else
        minx.toInt()

    val h_min =  if (miny <= 1)
        floor(srt.height() * miny).toInt() // 90% von Image width
    else
        miny.toInt()



    if (searchRect.width < w_min || searchRect.height < 12)
        return clone

    if (clone.isEmpty()){
        clone.add(cout(rect))
        return clone
    }




val out = filter(clone,rect)



    if (out.isEmpty())
          clone.add(cout(rect))


    return clone


}

fun filter (clone :ArrayList<cout>,suche : RotatedRect ): List<cout> {
    var filterout = false
    val searchRect = suche.boundingRect()
    val sx1 = searchRect.x // get the x coordinate of the top-left corner of the rectangle
    val sy1 = searchRect.y // get the y coordinate of the top-left corner of the rectangle
    val sx2 = searchRect.x + searchRect.width // get the x coordinate of the bottom-right corner of the rectangle
    val sy2 = searchRect.y + searchRect.height // get the y coordinate of the bottom-right corner of the rectangle


    val out = clone.filter {
        val boundingRect = it.rect.boundingRect()

        val x1 = boundingRect.x // get the x coordinate of the top-left corner of the rectangle
        val y1 = boundingRect.y // get the y coordinate of the top-left corner of the rectangle
        val x2 = boundingRect.x + boundingRect.width // get the x coordinate of the bottom-right corner of the rectangle
        val y2 = boundingRect.y + boundingRect.height // get the y coordinate of the bottom-right corner of the rectangle
        if (sx1 in x1..x2  && sx2 in x1..x2 && sy1 in y1..y2 && sy2 in y1..y2)
        {
            it.inside.add(suche)
            filterout= true
        }
        else
        {
            if ((sx1 in x1..x2 &&  sy1 in y1..y2) || (sx2 in x1..x2 && sy2 in y1..y2)  ){
                it.overlap.add(suche)
                filterout = true
            }

        }

        filterout
    }.toList()
    return out
}

fun filter1 (org :RotatedRect,suche : RotatedRect ): Boolean {
    val searchRect = suche.boundingRect()
    val sx1 = searchRect.x // get the x coordinate of the top-left corner of the rectangle
    val sy1 = searchRect.y // get the y coordinate of the top-left corner of the rectangle
    val sx2 = searchRect.x + searchRect.width // get the x coordinate of the bottom-right corner of the rectangle
    val sy2 = searchRect.y + searchRect.height // get the y coordinate of the bottom-right corner of the rectangle

        val boundingRect = org.boundingRect()
        val x1 = boundingRect.x // get the x coordinate of the top-left corner of the rectangle
        val y1 = boundingRect.y // get the y coordinate of the top-left corner of the rectangle
        val x2 = boundingRect.x + boundingRect.width // get the x coordinate of the bottom-right corner of the rectangle
        val y2 = boundingRect.y + boundingRect.height // get the y coordinate of the bottom-right corner of the rectangle
    return sx1 in x1..x2  && sx2 in x1..x2 && sy1 in y1..y2 && sy2 in y1..y2
}



fun BufferedImage.getSubimage(rect : RotatedRect): BufferedImage {
    val r = rect.boundingRect()
    return getSubimage(r.x,r.y,r.width,r.height)

}

@Throws(IOException::class)
fun BufferedImage.BufferedImage2Mat(): Mat {
    val byteArrayOutputStream = ByteArrayOutputStream()
    ImageIO.write(this, "jpg", byteArrayOutputStream)
    byteArrayOutputStream.flush()
    return Imgcodecs.imdecode(MatOfByte(*byteArrayOutputStream.toByteArray()), IMREAD_UNCHANGED)
}

fun findContours(srt: Mat, mode: Int, chainApproxSimple: Int ,point : Point? = null , removeShadowObject : Boolean = true): Pair<List<MatOfPoint>, Mat> {
    val contours  = arrayListOf<MatOfPoint>()
    val hierarchey = Mat()
if (point == null)
    Imgproc.findContours(srt,contours, hierarchey, mode ,chainApproxSimple )
    else
    Imgproc.findContours(srt,contours, hierarchey, mode ,chainApproxSimple ,point)

    val removeShadow = removeShadowObject(srt,contours)
    val c = contours.removeAll(removeShadow)

    return Pair(contours.toList(),hierarchey)
}



fun grab_contours(contours: List<MatOfPoint>): MutableList<MatOfPoint> {
    val con: MutableList<MatOfPoint> = mutableListOf()
    for (c in contours) {
        con.add(c)
    }
    return con
}

fun findLargestArea(contours: List<MatOfPoint>): Pair<Double, MatOfPoint> {
    // Find the contour with the largest area
    var maxArea = 0.0
    var maxContour: MatOfPoint = MatOfPoint()
    for (contour in contours) {
        val area = Imgproc.contourArea(contour)
        if (area > maxArea) {
            maxArea = area
            maxContour = contour
        }
    }
    return Pair(maxArea,maxContour)
}


data class contourArea(val matOfPoint: MatOfPoint, val countArea: Double)




fun List<MatOfPoint>.findMaxArea(srt: Mat = Mat()): Triple<Double, List<contourArea>, ArrayList<contourArea>> {


    var out = arrayListOf<contourArea>()
    var max = Double.MIN_VALUE
    for (i in this) {
        if (!srt.empty())
        {
            val rotatedRect = Imgproc.minAreaRect(i.toMatOfPoint2f()).boundingRect()

            val minW = srt.width()*0.95
            val minH = srt.height()*0.98

            if (rotatedRect.x+rotatedRect.width > minW || rotatedRect.y+rotatedRect.height >= minH)
                continue

        }


       val cont_area = Imgproc.contourArea(i)
        max = max.coerceAtLeast(cont_area)
        out.add(contourArea(i,cont_area))
    }
    val filter = out.filter {it.countArea == max }
    out.removeAll(filter.toSet())
    return Triple(max,filter,out)

}





fun findMin(list: List<Int>): Int {
    var min = Int.MAX_VALUE
    for (i in list) {
        min = min.coerceAtMost(i)
    }
    return min
}

fun findMax(list: List<Int>): Int {
    var max = Int.MIN_VALUE
    for (i in list) {
        max = max.coerceAtLeast(i)
    }
    return max
}


fun Mat.polylines( pts: List<MatOfPoint?>?, isClosed: Boolean, color: Scalar, thickness: Int, lineType: Int): Mat {
    val draw = Mat.zeros(this.size(), CvType.CV_8UC3)
    polylines(
        draw,
        pts,
        isClosed,
        color,
        thickness,
        lineType
    )
    return draw
}

fun RotatedRect.points(): Array<Point> {
    val pt: MutableList<Point> = mutableListOf()
    val _angle: Double = this.angle * Math.PI / 180.0
    val b = Math.cos(_angle) * 0.5
    val a = Math.sin(_angle) * 0.5
    pt.add(Point(
        this.center.x - a * this.size.height - b * this.size.width,
        this.center.y + b * this.size.height - a * this.size.width
    ))
    pt.add(Point(
        this.center.x + a * this.size.height - b * this.size.width,
        this.center.y - b * this.size.height - a * this.size.width
    ))
    pt.add(Point(2.0 * this.center.x - pt[0].x, 2.0 * this.center.y - pt[0].y))
    pt.add(Point(2.0 * this.center.x - pt[1].x, 2.0 * this.center.y - pt[1].y))
    return pt.toTypedArray()
}









private fun Mat.checkblur(mask: Size, sigmax: Double = 0.0 ,schalterBlur : Int = 0): Mat {

    val img_blur = when (schalterBlur) {
        0 -> this
        1 -> this.blur(mask)
        2 -> this.medianBlur(mask.height.toInt())
        else -> this.GaussianBlur(mask, sigmax)
    }
    return img_blur
}

fun Mat.medianBlur(mask : Int = 3): Mat {
    val blurred1 = Mat()
    Imgproc.medianBlur(this, blurred1, mask)
    return blurred1
}


fun Mat.blur(ksize : Size = Size(3.0, 3.0)): Mat {
    val blurred1 = Mat()
    blur(this, blurred1, ksize)
    return blurred1
}


fun Mat.GaussianBlur(size: Size =  Size(3.0, 3.0), sigmax: Double = 0.0): Mat {
    val out = Mat()
    GaussianBlur(this, out, size, sigmax)
    return out
}




fun Mat.findSquares(removeShadow : Boolean = true): ArrayList<MatOfPoint> {

    val squares: ArrayList<MatOfPoint> = arrayListOf()
    // blur will enhance edge detection
    val blurred =  this.medianBlur(9)
    val gray0 = Mat(blurred.size(), CvType.CV_8U)
    val contours: List<MatOfPoint> = ArrayList()



    // find squares in every color plane of the image
    for (c in 0..2) {
        val ch = intArrayOf(c, 0)
        Core.mixChannels(listOf(blurred), listOf(gray0), ch.toMatOfInt())

        // try several threshold levels
        val thresholdLevel = 3
        for (l in 0 until thresholdLevel) {

          val gray =  when(l){

               1 -> gray0.withthreshold1(l,thresholdLevel)
               2 ->  gray0.withthreshold2()
                else -> gray0.withCanny()

            }



            // Find contours and store them in a list
            Imgproc.findContours(gray, contours, Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

            // Test contours
            for (contour in contours) {
                // approximate contour with accuracy proportional
                // to the contour perimeter
                val approx = MatOfPoint2f()
                val contourMat = contour.toMatOfPoint2f()
                val epsilon = 0.02 * Imgproc.arcLength(contourMat, true)
                approxPolyDP(contourMat, approx, epsilon, true)

                val approx1 = MatOfPoint2f()

                val peri = Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
                Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx1, 0.04 * peri, true)

                when (approx1.toList().size) {
                    4 -> {
                        val rect = Imgproc.boundingRect(contour)
                        val aspectRatio = rect.width.toDouble() / rect.height
                        if (aspectRatio in 0.95..1.05) {   /// ist ein Quadrad ?
                            continue
                        }
                    }
                    else -> continue
                }


                // Note: absolute value of an area is used because
                // area may be positive or negative - in accordance with the
                // contour orientation
                if (approx.size().height.toLong() == 4L &&
                    abs(contourArea(approx)) > 5000 &&
                    Imgproc.isContourConvex(approx.toMatOfPoint() )) {
                    var maxCosine = 0.0

                    for (j in 2..4) {
                        val pt0 = approx.toListPoint()[j % 4]
                        val pt1 = approx.toListPoint()[j -2]
                        val pt2 = approx.toListPoint()[j -1]
                        val cosine = abs(angle(pt0, pt1, pt2))
                        maxCosine = max(maxCosine, cosine)
                    }

                    val rect = Imgproc.boundingRect(contour)
                    if (rect.x > gray.width() * 0.7  || rect.x+rect.width < gray.width() * 0.2 || rect.y > gray.height()*0.8 || rect.y+rect.height < gray.height()*0.1 )
                        continue

                    if (maxCosine > 0.2)
                        squares.add(approx.toMatOfPoint())


                }
            }
        }
    }


    return if(removeShadow) removeShadowObject(gray0,squares) else squares
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

fun Mat.withthreshold1(l:Int,thresholdLevel : Int): Mat {
    val gray = Mat()
    this.copyTo(gray)
    Imgproc.threshold(gray, gray, (l+1) * 255 / thresholdLevel.toDouble(), 255.0, Imgproc.THRESH_BINARY)
    return gray
}

fun Mat.withthreshold2(): Mat {
    val th3 = adaptiveThreshold(this,255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY,199,15) .apply {dilate(this,
        Main.kernel,2)  }
    return th3
}
fun Mat.withthreshold2_INV(): Mat {
    val th3 = adaptiveThreshold(this,255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY_INV,199,15) .apply {dilate(this,
        Main.kernel,2)  }
    return th3
}




private fun angle(pt1: Point, pt2: Point, pt0: Point): Double {
    val dx1 = pt1.x - pt0.x
    val dy1 = pt1.y - pt0.y
    val dx2 = pt2.x - pt0.x
    val dy2 = pt2.y - pt0.y
    return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1*dx1 + dy1*dy1) * (dx2*dx2 + dy2*dy2) + 1e-10)
}






fun Mat.findMinValue(): Pair<Int, Double> {
    var minDist = Double.MAX_VALUE
    var brIndex = 0
    for (i in 0 until this.rows()) {
        val dist = this.get(i, 0)[0]
        if (dist < minDist) {
            minDist = dist
            brIndex = this.get(i, 1)[0].toInt()
        }
    }
    return Pair(brIndex,minDist)
}



fun dist(p1: Point, p2: Point): Double {
    return distanceBetweenPoints(Pair( p1.x, p1.y), Pair( p2.x, p2.y))
}



fun distanceBetweenPoints(p1: Pair<Double, Double>, p2: Pair<Double, Double>): Double {
    val xDiff = p1.first - p2.first
    val yDiff = p1.second - p2.second
    return kotlin.math.sqrt(xDiff * xDiff + yDiff * yDiff)
}

fun sortContours(cnts: List<MatOfPoint>, method: String = "left-to-right"): Pair<List<MatOfPoint>, List<Rect>> {
    var reverse = false
    var i = 0
    if (method == "right-to-left" || method == "bottom-to-top") {
        reverse = true
    }
    if (method == "top-to-bottom" || method == "bottom-to-top") {
        i = 1
    }
    val boundingBoxes = cnts.map { Imgproc.boundingRect(it) }
    val sorted = cnts.zip(boundingBoxes).sortedBy {
        when (i)
        {
            1-> it.second.y
            else ->it.second.x
        }
       }


    return if (reverse) sorted.reversed().unzip() else sorted.unzip()
}


fun ArrayList<MatOfPoint>.findMaxAreaPos(): Pair<Int, ArrayList<ContourAreaH>> {
    val gl_neu = arrayListOf<ContourAreaH>()

    for (contour in this) {
        val cont_area = contourArea(contour)
        val rect =  contour.toRotatedRect()
        gl_neu.add(ContourAreaH(contour,cont_area,rect))
    }

        var maxDist = Double.MIN_VALUE
        var indexPos = 0
        for (i in  this.indices) {

            if (gl_neu[i].area > maxDist){
                    indexPos = i
            }
            maxDist = maxDist.coerceAtLeast(gl_neu[i].area)


        }


      return Pair(indexPos,gl_neu)




}




@JvmName("isInside2")
fun RotatedRect.isInside(isInsideRotatedRect: RotatedRect): Boolean {
    return this.isInside(isInsideRotatedRect.boundingRect())
}

@JvmName("isInside3")
fun Rect.isInside(isInsideRect: RotatedRect): Boolean {
    return this.isInside(isInsideRect.boundingRect())
}

@JvmName("isInside1")
fun RotatedRect.isInside(isInsideRect: Rect): Boolean {
    return this.boundingRect().isInside(isInsideRect)
}

fun Rect.isInside(isInsideRect: Rect): Boolean {
    val orginal = this
    val search =isInsideRect
    val sx1 = search.x // get the x coordinate of the top-left corner of the rectangle
    val sy1 = search.y // get the y coordinate of the top-left corner of the rectangle
    val sx2 = search.x + search.width // get the x coordinate of the bottom-right corner of the rectangle
    val sy2 = search.y + search.height // get the y coordinate of the bottom-right corner of the rectangle

    val x1 = orginal.x // get the x coordinate of the top-left corner of the rectangle
    val y1 = orginal.y // get the y coordinate of the top-left corner of the rectangle
    val x2 = orginal.x + orginal.width // get the x coordinate of the bottom-right corner of the rectangle
    val y2 = orginal.y + orginal.height // get the y coordinate of the bottom-right corner of the rectangle
    return sx1 in x1..x2  && sx2 in x1..x2 && sy1 in y1..y2 && sy2 in y1..y2
}



fun  removeShadowObject(srt: Mat, contour : List<MatOfPoint>, xytoleranz : Int = 2, whtoleranz : Int = 5): ArrayList<MatOfPoint> {
    return removeShadowObject(srt,contour.toArrayList() , xytoleranz,whtoleranz)
}
// filter Objekte die die selben Kordinate habe aber mit wenigen Pixel toleranz
fun  removeShadowObject(srt: Mat, contour : ArrayList<MatOfPoint>, xytoleranz : Int = 2, whtoleranz : Int = 5): ArrayList<MatOfPoint> {
    val near = arrayListOf<Int>()
    val nearObject = arrayListOf<MatOfPoint>()


    if (contour.isNullOrEmpty())
    {
        print("Keine ContourDaten vorhanden")
        return ArrayList(contour)
    }

    for ((i,item) in contour.withIndex())
    {

        if (near.find {it == i} != null)
            continue
        val cont_area = contourArea(item)
        if (cont_area < 8000){

            near.add(i)
            nearObject.add(item)
            continue
        }
        val mbR = item.toBoundingRect()
        val mX = if (mbR.x < 0) 0 else mbR.x
        val mY = if (mbR.y<0) 0 else mbR.y
        var mW = mbR.width + mbR.x
        var mH = mbR.height + mbR.y
        mW = if(mW > srt.width()) srt.width() else mW
        mH = if(mH > srt.height()) srt.height() else mH


        for ((i2,item2) in contour.withIndex()){
            if (i == i2) continue
            val search = item2.toBoundingRect()
            val sX = if (search.x < 0) 0 else search.x
            val sY = if (search.y < 0) 0 else search.y
            var sW = search.width + search.x
            var sH = search.height + search.y
            sW = if(sW > srt.width()) srt.width() else sW
            sH = if(sH > srt.height()) srt.height() else sH

            if ( sX in mX-2 .. mX+2 && sY in mY-2 .. mY+2 && sW in mW-5..mW+5 && sH in mH-5..mH+5  ){
                near.add(i2)
                nearObject.add(item2)
            }

        }


    }


    return nearObject
}

@Override
fun ArrayList<MatOfPoint>.copy(): ArrayList<MatOfPoint> {
    val neu =  arrayListOf<MatOfPoint>()
        neu.addAll(this)
    return neu
}

fun List<MatOfPoint>.removeAll(list : ArrayList<MatOfPoint>): List<MatOfPoint> {
    val x = this.toArrayList()
    x.removeAll(list)
    return x.toList()
}


fun corectRect(srt : Mat , search: Rect): Rect {
    val sW = search.width + search.x
    val sH = search.height + search.y

    if (sW > srt.width()) search.width = srt.width() -  if(search.x < 0)search.x else 0  // zuerst weite und höhe korreKtieren
    if (sH > srt.height()) search.width = srt.height()- if(search.y < 0)search.y else 0
    if (search.x < 0) search.x = 0   // wenn x kleiner als 0 = 0
    if (search.y < 0) search.y = 0  // wenn y kleiner als 0  = 0

    return search
}

