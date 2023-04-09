package library.classes

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect

  // python    img[100:100,60:60]
fun Mat.copyArea(region : Rect): Mat {

    val imageWight = this.width()
    val imageHight = this.height()

    val x = region.x
    val y = region.y
    val max_w = x + region.width
    val max_h = y+ region.height

    if (max_w > imageWight)
        println("Ausschnitt ist zu weit")
    if (max_h > imageHight)
        println("Ausschnitt zu zu hoch")

   return Mat(this, region)
}

fun Mat.isOutsideImage(rect : Rect): Pair<Boolean, ArrayList<String>> {
    var msg = arrayListOf<String>()
    var error = false

    val imageWight = this.width()
    val imageHight = this.height()

    val x = rect.x
    val y = rect.y
    val max_w = x + rect.width
    val max_h = y+ rect.height

    if (max_w > imageWight){
        msg.add("Ausschnitt ist zu weit")
        error = true
    }

    if (max_h > imageHight) {
        msg.add("Ausschnitt zu zu hoch")
        error = true
    }

    return Pair(error , msg)
}


// python     img[100:23,100:20]   =      Mat img[100:100,60:60]


@JvmName("insertTo2")
fun insertTo(dst:Mat ,insertarea : Mat,toArea : Point ): Mat {
    val area = Rect(toArea.x.toInt(), toArea.y.toInt(),insertarea.width(),insertarea.height())
    val img = dst.clone()
    insertarea.copyTo(Mat(img, area))
    return img
}


@JvmName("insertTo3")
fun insertTo(dst:Mat ,insertarea : Mat ,toArea : Rect ): Mat {
    val img = dst.clone()
    val ballCopy = Mat()
    insertarea.copyTo(ballCopy)
    ballCopy.copyTo(Mat(img, toArea))
    return img
}
fun Mat.insertTo(dst:Mat  ,toArea : Rect ): Mat {
    val img = dst.clone()
    val ballCopy = Mat()
    this.copyTo(ballCopy)
    ballCopy.copyTo(Mat(img, toArea))
    return img
}

fun Mat.insertTo(dst:Mat ,toArea : Point ): Mat {
    val area = Rect(toArea.x.toInt(), toArea.y.toInt(),this.width(),this.height())
    val img = dst.clone()
    this.copyTo(Mat(img, area))
    return img
}