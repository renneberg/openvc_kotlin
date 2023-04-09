package library

import classes.numpy
import library.classes.*
import library.converters.*
import org.opencv.core.*
import org.opencv.core.CvType.CV_8UC1
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import java.util.*
import kotlin.collections.ArrayList



object Main : MouseListener {
    val cv = cv2()
    val dir = File("/home/zermos/Desktop/Orginal/4/80001/").listFiles()

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    val callback = fun(x:Int){
        val resImage = globalImage
        val hsv = resImage.cvtColor(COLOR_BGR2HSV)
        val l_h = cv.getTrackbarPos("LH")
        val l_s = cv.getTrackbarPos("LS")
        val l_v = cv.getTrackbarPos("LV")
        val u_h = cv.getTrackbarPos("UH")
        val u_s = cv.getTrackbarPos("US")
        val u_v = cv.getTrackbarPos("UV")

        val l_b = np.array(l_h,l_s,l_v)
        val u_b = np.array(u_h,u_s,u_v)
        val mask1 = hsv.inRange(l_b,u_b)
        val res = bitwise_and(resImage, resImage,mask1)

        cv.addImage("make Gray", res.resize(.11))

    }


    val np = numpy()
    var globalImage = Mat()
    var orginal = Mat()
    val kernel : Mat = np.ones(Size(1.0,1.0),CV_8UC1)




    @JvmStatic
    fun main(args: Array<String>) {
        for (item in args) {
            print(item)
        }

        with(cv) {
            val leer = Mat(Size(100,100),CV_8UC1)
            addImage("bild1","test",leer)
            addImage("Orginal","Orginal",leer)
            addImage("Copy","Orginal",leer)
       //     addImage("result1","result1",leer)
            cv.addImage("bild12","test2",leer)
            cv.addImage("Global","Global",leer)


            namedWindows("tracking")
            createTrackbar("LH","tracking",0,255,callback)
            createTrackbar("LS","tracking",0,255,callback)
            createTrackbar("LV","tracking",0,255,callback)
            createTrackbar("UH","tracking",0,255,255,callback)
            createTrackbar("US","tracking",0,255,255,callback)
            createTrackbar("UV","tracking",0,255,255,callback)
            addImage("make Gray","tracking",Mat(400,400, CV_8UC1))

            setMouseListener(this@Main)
            imshow()
        }




for (item in dir)
{
if (item.isDirectory) return
orginal = imread(item.toString())
    globalImage =  imread(item.toString())
val copia = imread(item.toString())
val img = orginal.cvtColor(COLOR_BGR2GRAY)



   val  (B, G, R) = copia.split()

    cv.addImage("Red","Red",R.resize(.11))
    cv.addImage("Blue","Blue",B.resize(.11))
    cv.addImage("Green","Green",G.resize(.11))
    cv.addImage("Gray","Gray",copia.cvtColor(COLOR_BGR2GRAY) .resize(.11))
    
    cv.imshow()
    cv.keywait()
    continue


    val removelist = arrayListOf<ContourAreaH>()  // zum löschen von nicht gebrauchten Arreas

// Draw a rectangle around each contour
        val result = copia.clone()
        val thickness = 30
        val color = Scalar(0.0, 255.0, 0.0) // Green

    val finde_alle_Contours = globalImage.findSquares()


// finde die Größte Fläche
    val (pos , list) = finde_alle_Contours.findMaxAreaPos()

    val maxArea = list[pos].rect.boundingRect()
    var search = Rect()   // Global Search Rect for draw

    if (maxArea.height > globalImage.height()*0.6)
      continue


    for ((index,item) in list.withIndex()){
        if (pos == index)
            continue

        search =  corectRect(globalImage,item.toRect())


        if (maxArea.isInside(search)) {
            removelist.add(item)
            continue
        }

    }

    list.removeAll(removelist.toSet())


    if (list.size > 1){

        val x = copia.clone()
        for ((index,item) in list.withIndex())
        {

            rectangle(x, item.toRect(), Scalar(Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)), thickness)
        }
        cv.addImage("item5test","item5test",x.resize(.12))
        cv.imshow("item5test")
        cv.keywait()
    }



    for (item in list)
    {
        rectangle(globalImage,  corectRect(globalImage,item.toRect()), Scalar(255,0,0), thickness)
    }







     for (contour in finde_alle_Contours) {
         val cont_area = contourArea(contour)
         val Moments = moments(contour)

         // Mittelpunkt ermitteln
         val x = (Moments._m10 / Moments._m00).toInt()
         val y = (Moments.m01 / Moments.m00).toInt()

         val mitte = Point(x, y)

         val approx = MatOfPoint2f()

         val peri = Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
         Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, 0.04 * peri, true)

         circle(result, mitte, 30, Scalar(255, 0, 0),-1, LINE_4)



         // classify the shape based on the number of vertices
         when (approx.toList().size) {
             3 -> {
               //  println("Triangle")

                 putText(result,"Triangle",mitte, FONT_HERSHEY_SIMPLEX,5.0, Scalar(39,255,100),20)
                  result.polylines(contour.toListOfMatOfPoint(), true, Scalar(0, 0, 255), 100, LINE_4)
                 continue
             }

             4 -> {
                 val rect = Imgproc.boundingRect(contour)
                 val aspectRatio = rect.width.toDouble() / rect.height
                 if (aspectRatio in 0.95..1.05) {
           //          println("Square")

                     putText(result,"Square",mitte, FONT_HERSHEY_SIMPLEX,5.0, Scalar(39,255,100),20)

                     result.polylines(contour.toListOfMatOfPoint(), true, Scalar(0, 0, 255), 100, LINE_4)
                 } else {
             //        println("Rectangle")
                     putText(result,"Rectangle",mitte, FONT_HERSHEY_SIMPLEX,5.0, Scalar(39,255,100),20)


                         result.polylines(contour.toListOfMatOfPoint(), true, Scalar(0, 0, 255), 50, LINE_4)
                 }
             }

             5 -> {
              //   println("Pentagon")

                 putText(result,"Pentagon",mitte, FONT_HERSHEY_SIMPLEX,5.0, Scalar(39,255,100),20)
                  result.polylines(contour.toListOfMatOfPoint(), true, Scalar(0, 0, 255), 50, LINE_4)
             }

             else -> {
              //   println("Circle")
                 putText(result,"Circle",mitte, FONT_HERSHEY_SIMPLEX,5.0, Scalar(39,255,100),20)

                 result.polylines(contour.toListOfMatOfPoint(), true, Scalar(0, 0, 255), 50, LINE_4)
             }

         }




         for (contour in finde_alle_Contours) {
             val rect = Imgproc.boundingRect(contour)
             rectangle(result, rect, color, thickness)
         }
     }
    cv.addImage("Global",result.resize(.11))

val th3 = adaptiveThreshold(img,255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY,199,15) .apply {dilate(this,kernel,2)  }

    cv.addImage("bild12",result.resize(.15))
    cv.addImage("result","result",th3.resize(.11))
    cv.addImage("bild1","test",img.resize(.11))



  var (contours, hierarchey) = findContours(th3,  RETR_TREE, CHAIN_APPROX_NONE, Point(-1,-1))

    var draw: Mat = Mat.zeros(orginal.size(), CvType.CV_8UC3)


val find = arrayListOf<RotatedRect>()
    val neu :  ArrayList<MatOfPoint> = arrayListOf()

for (i in contours.indices) {
    val cont_area = contourArea(contours[i])
   val Moments = moments(contours[i])

   val x = (Moments._m10   / Moments._m00 ).toInt()
   val y = (Moments.m01 / Moments.m00).toInt()



    if (cont_area < 8000)
       continue
   //     println("\n" + cont_area)

    val minArea = contours[i].toBoundingRect()
    val minAreaporlist = contours[i].toRotatedRect()

    if (minArea.x > draw.width()*0.8)
    {
   //     print("beginnt weiter  als 80%")
        continue
    }

    if (minArea.x+minArea.width < draw.width()*0.1)
    {
   //     print("beginnt weiter  als 80%")
        continue
    }

    if (minArea.width < draw.width()*0.1)
        continue
    circle(draw, Point(x, y), 20, Scalar (255, 255, 255), -1)

    find.add(minAreaporlist)
    neu.add (contours[i])

    polylines(draw,contours[i].toListOfMatOfPoint(),true, Scalar(0,255,0),10, FILLED)
}
HighGui.imshow("Contours operation", draw.resize(.11))
HighGui.waitKey(1);

val mylist : ArrayList<cout> = arrayListOf()


for (i in find)
{
   val  (err , msg) = orginal.isOutsideImage(i.boundingRect())

    if (!err)
        mylist.sortRect(orginal,i,0.1,0.3)


}



val remove = arrayListOf<cout>()


  mylist.filter {seach ->
    val neu = mylist.filter{
        var x = false
        if (seach.rect != it.rect)
        {
          x =  filter1(seach.rect,it.rect)
            if (x)
            {
               seach.inside.add(it.rect)
               seach.inside.addAll(it.inside)
               seach.overlap.addAll(it.overlap)
               remove.add(it)
            }

        }

   x

    }


 neu.isNotEmpty()
}


if (remove.isNotEmpty())
mylist.removeAll(remove)





for (item in mylist){


    for (item2 in item.inside){
        val x = item2
        drawRotatedRectangle(copia, x,20, Scalar(255,0,0))
    }
    for (item2 in item.overlap){
        val x = item2
        drawRotatedRectangle(copia, x,20, Scalar(0,0,255))
    }

   if ( item.inside.isNotEmpty())
   {
       if (mylist.size <= 5)
           println(mylist)

       println("\nsize =${mylist.size} inside = ${item.inside.size}  overlap= ${item.overlap.size}   ${item.rect}   ")
       val x = item.rect
       drawRotatedRectangle(copia, x,50,Scalar(0,255,0))
   }

}

for ((index,item) in mylist.withIndex())
{
    if (index > 5)
        break
    try {
             val cut = orginal.copyArea(item.rect.boundingRect())
      //  imwrite("xx${getTime()}$index.jpg",cut)





       val neu =  orginal.Mat2BufferedImage()

        val h = orginal.width()
        val r = orginal.height()

  val x=      item.rect.boundingRect().x
       val y = item.rect.boundingRect().y
      val w =  item.rect.boundingRect().width
      val h1 =  item.rect.boundingRect().height
        val w_x = x + w
        val h_y = y + h1

        if (w_x > h)
            println("zu weit")

            if (h_y > r)
                println("zu Hoch")

       val cut1 =  neu.getSubimage(item.rect)
        val ma = cut1.BufferedImage2Mat()
   //     imwrite("neueBilder/xx${getTime()}$index.jpg",ma)
    } catch (e: Exception) {
        print(e)
    }
}


cv.addImage("Orginal",orginal.resize(.11))
cv.addImage("Copy",copia.resize(.11))


callback(3)
cv.keywait(1)



}


}





    override fun mouseClicked(p0: MouseEvent?) {

    }

    override fun mousePressed(p0: MouseEvent?) {

    }

    override fun mouseReleased(p0: MouseEvent?) {
        if (p0 != null) {
            if (p0.button == MouseEvent.BUTTON2) {
                cv.mouseClicked(p0)
            }
        }

    }

    override fun mouseEntered(p0: MouseEvent?) {

    }

    override fun mouseExited(p0: MouseEvent?) {

    }


}


