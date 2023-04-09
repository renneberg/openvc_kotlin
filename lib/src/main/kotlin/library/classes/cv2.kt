package library.classes


import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import javax.swing.*
import kotlin.math.roundToInt
import java.awt.Color
import java.awt.color.ICC_ColorSpace


data class Frames(val s : String, val frame : JFrame ,val firstPanel : JPanel, val component: ArrayList<Component> = arrayListOf())
data class Trackbars(val s : String , val trackbar : JSlider)
data class ImageList(val name : String ,val firstPanel: JPanel ,var jLabel: JLabel = JLabel())

class cv2 : KeyListener {

   private val windows = arrayListOf<Frames>()  // alle Hauptfenster
   private val trackbarAll = arrayListOf<Trackbars>()
    private var ImagenList = arrayListOf<ImageList>()
    var wait = true




    fun keywait(sek : Int = 0){
        val firstTime = getTime()

        for (item in windows)
        {
            item.frame.addKeyListener(this)
        }

        while (wait) {
            val dif = (getTime() - firstTime)
            if (sek > 0) {

                if (dif / 1000 >= sek)
                    break
            }
        }

        for (item in windows)
        {
            item.frame.removeKeyListener(this)
            wait = true
        }

    }


    fun namedWindows(sFrameName: String, flag: LayoutManager = BorderLayout(), b: Boolean = true): JPanel {
        val frame = JFrame(sFrameName).apply {
            layout = flag
            pack()
            minimumSize = Dimension(300,200)
            if (b)
                defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setLocationRelativeTo(null)
        }

        val panel = JPanel().apply {
            alignmentX = Component.LEFT_ALIGNMENT
            alignmentY = Component.TOP_ALIGNMENT
            val layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
            this.layout = layout
        }

        frame.add(panel)


       return if (!existWindows(sFrameName)) {
            windows.add(Frames(sFrameName , frame, panel))
            panel
        }
        else
            getWindows(sFrameName)!!.firstPanel
    }


    fun createTrackbar(label: String, sFrameName: String, min: Int, max: Int , callback: (Int) -> Unit){
        createTrackbar(label,sFrameName,min, max,0, callback)
    }

    fun createTrackbar(label: String, sFrameName: String, min: Int, max: Int,initial : Int , callback: (Int) -> Unit){

     val pos =  getWindowsPos(sFrameName)
        if (pos == -1) return



        val v = windows[pos].firstPanel


        val myLabel = JLabel(label).apply {
            setSize(size.width + 100,size.height)
        }
                val panel = JPanel()
                panel.layout = FlowLayout()
                val slider = JSlider().apply {
                    layout = FlowLayout(1, 100, 100)
                    majorTickSpacing = 1
                    //  paintTicks = true
                    isVisible = true
                    minimum = min
                    maximum = max
                     value = initial
                    addChangeListener {
                        myLabel.text = label +" ${this.value}"
                        callback(this.value);
                    }
                }
                panel.add(myLabel)
                panel.add(slider)

                v.add(panel)


         trackbarAll.add(Trackbars(label,slider))

    }

    fun imshow(){
        for (item in windows)
            item.frame.apply {
                this.name = item.s

                    isVisible = false

                revalidate()
                pack()
                repaint()
                isVisible = true
            }

    }

    fun imshow(s : String){
        getWindows(s)?.apply {
            this.frame.name = s
            this.frame.isVisible = true
        }
    }

    fun imshow(s : String,src : Mat){

        if (existWindows(s))
        {
            getWindows(s)?.apply {
                for (item in frame.components)
                    when (item)
                    {
                        is JLabel -> {
                            item.icon =   ImageIcon(HighGui.toBufferedImage(src).getScaledInstance(src.width(), src.height(), Image.SCALE_SMOOTH))


                            this.frame.isVisible = false

                            frame.revalidate()
                            frame.pack()
                            frame.repaint()
                            this.frame.isVisible = true
                            return
                        }
                    }
            }
        }
        else {
            val label = JLabel(ImageIcon(
                HighGui.toBufferedImage(src).getScaledInstance(src.width(), src.height(), Image.SCALE_SMOOTH)
            ))
            addComponent(s,label)

        }

        getWindows(s)?.frame?.apply {
            isVisible = false

            size = Dimension(src.width(), src.height())
           revalidate()
            pack()
            repaint()
           isVisible = true
        }


    }


    fun addComponent(sPanelName: String, com: JComponent)
    {
        var pos = getWindowsPos(sPanelName)
        if (pos == -1) {
            namedWindows(sPanelName, b = true)
            pos = getWindowsPos(sPanelName)
        }
        windows[pos].frame.name = sPanelName
        windows[pos].frame.add(com)

    }





  private fun existWindows(s:String): Boolean {
      return windows.filter { it.s == s }.isNotEmpty()
  }

  private fun getWindows(s:String): Frames? {
      return windows.find { it.s == s }
  }

 private  fun getWindowsPos(s:String): Int {
     for ((index,item) in windows.withIndex())
     {
         if (item.s == s)
             return index
     }
     return -1
 }

 fun getTrackbarPos(sLabel : String): Int {
     return trackbarAll.find { it.s == sLabel }?.trackbar?.value ?: -1
 }


    fun addImage(name: String , jFrame: String , src : Mat){
        val count = ImagenList.getPos(name)
        if (count > -1) {
            ImagenList[count].jLabel.icon =
                ImageIcon(HighGui.toBufferedImage(src).getScaledInstance(src.width(), src.height(), Image.SCALE_SMOOTH))

            val frame : JFrame = getWindows(jFrame)!!.frame

            frame.revalidate()
            frame.pack()
            frame.repaint()


            return

        }

        val img = JLabel(
            ImageIcon(
                HighGui.toBufferedImage(src).getScaledInstance(src.width(), src.height(), Image.SCALE_SMOOTH)
            )
        )

        val win : JPanel= getWindows(jFrame)?.firstPanel ?:   namedWindows(jFrame)
        ImagenList.add(ImageList(name,win ,img))
        win.add(img)

/*        val frame : JFrame = getWindows(jFrame)!!.frame
        frame.revalidate()
        frame.pack()
        frame.repaint()*/


    }



    fun addImage(name: String , src : Mat){
        val count = ImagenList.getPos(name)
        if (count > -1) {
            ImagenList[count].jLabel.icon =
                ImageIcon(HighGui.toBufferedImage(src).getScaledInstance(src.width(), src.height(), Image.SCALE_SMOOTH))

            return
        }

    }





 fun bufferedImageToMat(bi: BufferedImage): Mat {
        val mat = Mat(bi.height, bi.width, CvType.CV_8UC3)
        val data = (bi.raster.dataBuffer as DataBufferByte).data
        mat.put(0, 0, data)
        return mat
    }


    fun ArrayList<ImageList>.getPos(name: String): Int {
        for ((index, c) in this.withIndex()) {
            if (c.name == name)
                return index
        }
        return -1
    }

    override fun keyTyped(p0: KeyEvent?) {
        print("\n" + p0)

    }

    override fun keyPressed(p0: KeyEvent?) {


    }

    override fun keyReleased(p0: KeyEvent?) {
        wait = false
    }


    fun mouseClicked(e: MouseEvent) {
        val p: Point = e.getLocationOnScreen()
        System.out.println("Pixel:" + p.x + "," + p.y)
        try {
            val pic = getPixel(p.x, p.y)
            if (pic != null) {
                println("${pic.toHSV().toString()} " + getPixel(p.x, p.y))
            }
        } catch (e1: AWTException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
    }



    @Throws(AWTException::class)
    fun getPixel(x: Int, y: Int): Color? {
        val rb = Robot()
        return rb.getPixelColor(x, y)
    }

    fun setMouseListener(call : MouseListener){
        for (win in windows)
            win.firstPanel.addMouseListener(call)

    }

    fun Color.toHSV() {
        // RGB color values between 0 and 255
        val red = 40
        val green = 32
        val blue = 28


        val rgb = intArrayOf(red, green, blue)


    val hsv = FloatArray(3)
    Color.RGBtoHSB(red, green, blue, hsv)





        // Round HSV values and convert to range 0-255 for saturation and value, 0-360 for hue
        val hue = hsv[0].times(360).roundToInt()
        val saturation = hsv[1].times(255).roundToInt()
        val value = hsv[2].times(255).roundToInt()

        // Print the resulting HSV color
        println("HSV color: ($hue, $saturation, $value)")
    }

}



