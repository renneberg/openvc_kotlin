package library.classes

import org.opencv.core.Mat

@JvmName ("shape2")
fun shape(src: Mat): Shape {
    val height = src.rows()
    val width = src.cols()
    val channels: Int = src.channels()
    return Shape(height, width, channels)
}


fun Mat.shape(): Shape {
    val height = this.rows()
    val width = this.cols()
    val channels: Int = this.channels()
    return Shape(height, width, channels)
}

data class Shape(val height: Int, val width: Int, val channels: Int) {
    override fun toString(): String {
        return "($height,$width,$channels)"
    }
}

