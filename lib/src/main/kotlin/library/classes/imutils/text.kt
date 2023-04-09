package library.classes.imutils


import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*

fun putText(
    img: Mat,
    text: String,
    org: Point,
    fontFace: Int,
    fontScale: Double,
    color: Scalar,
    thickness: Int = 1,
    lineType: Int = LINE_8,
    bottomLeftOrigin: Boolean = false
)
{
    val (x, y) = org

    // Break text into list of text lines
    val textLines = text.split("\n")


    // Get height of text lines in pixels (height of all lines is the same)
   // require(!(baseLine != null && baseLine.size !== 1)) { "'baseLine' must be 'int[1]' or 'null'." }
    val (_, line_height)  = getTextSize("", fontFace, fontScale, thickness,IntArray(1))

    // Set distance between lines in pixels
    val line_gap = line_height / 3

    for ((i, textLine) in textLines.withIndex()) {
        // Find total size of text block before this line
        val lineYAdjustment = i * (line_gap + line_height)

        // Move text down from original line based on line number
        val lineY = if (bottomLeftOrigin) {
            y - lineYAdjustment
        } else {
            y + lineYAdjustment
        }

        // Draw text
        putText(
            img,
            textLine,
            Point(x, lineY),
            fontFace,
            fontScale,
            color,
            thickness,
            lineType,
            bottomLeftOrigin
        )
    }
}

fun putCenteredText(
    img: Mat, text: String, fontFace: Int, fontScale: Double,
    color: Scalar, thickness: Int = 1, lineType: Int = LINE_8
) {
    // Save img dimensions
    val (imgH, imgW) = img.size()

    // Break text into list of text lines
    val textLines = text.split("\n")

    // Get height of text lines in pixels (height of all lines is the same; width differs)
    val (_, lineH) = Imgproc.getTextSize("", fontFace, fontScale, thickness, IntArray(1))

    // Set distance between lines in pixels
    val lineGap = lineH / 3

    // Calculate total text block height for centering
    var textBlockH = textLines.size * (lineH + lineGap)
    textBlockH -= lineGap  // There's one less gap than lines

    for ((i, textLine) in textLines.withIndex()) {
        // Get width of text line in pixels (height of all lines is the same)
        val (lineW, _) = Imgproc.getTextSize(textLine, fontFace, fontScale, thickness, IntArray(0))

        // Center line with image dimensions
        val x = (imgW - lineW) / 2
        var y = (imgH + lineH) / 2

        // Find total size of text block before this line
        val lineAdjustment = i * (lineGap + lineH)

        // Adjust line y and re-center relative to total text block height
        y += lineAdjustment - textBlockH / 2 + lineGap.toInt()

        // Draw text
        val org = Point(x.toDouble(), y.toDouble())
        Imgproc.putText(img, textLine, org, fontFace, fontScale, color, thickness, lineType)
    }
}



operator fun  org.opencv.core.Size.component2(): Double {
return this.width
}

operator fun  org.opencv.core.Size.component1(): Double {
return this.height
}


private operator fun Point.component2(): Double {
return this.x
}

private operator fun Point.component1(): Double {
   return this.y
}


