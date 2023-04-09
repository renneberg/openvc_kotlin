package classes

import com.google.common.base.Preconditions
import com.google.common.collect.ContiguousSet
import com.google.common.collect.DiscreteDomain
import com.google.common.collect.Range
import library.classes.Scalar
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import java.util.function.BiFunction
import java.util.stream.Collectors
import java.util.stream.IntStream

class numpy {

    var pi = Math.PI

    fun cos(x : List<Double>): List<Double> {
        return x.map { xi -> kotlin.math.cos(xi) }.toList()

    }

    fun sin(x : List<Double>): List<Double> {
        return  x.map { xi -> kotlin.math.sin(xi) }.toList()
    }



    fun ones(size : Size,type : Int = CvType.CV_8UC1): Mat {
       return Mat.ones(size,type)
    }

 /*
 1 -4  letzt Zahl ändern
 CV_8UC1
 CV_8SC1

 CV_16UC1
 CV_16SC1
 CV_16FC1

 CV_32SC1
 CV_32FC1

 CV_64FC1
 */
    fun zeros(size: Size, type: Int): Mat {
        return Mat.zeros(size, type)
    }

    fun array(v0 : Int ,v1 : Int,v2: Int): Scalar {
        return  Scalar(v0,v1,v2)
    }

    // TODO: more options from numpy
    fun linspace(start: Double, end: Double, num: Int): List<Double> {
        Preconditions.checkArgument(num >= 0)
        return ContiguousSet.create(Range.closedOpen(0, num), DiscreteDomain.integers())
            .stream().map { x: Int -> x * (end - start) / (num - 1) + start }.collect(Collectors.toList())
    }

    fun arange(start: Double, end: Double, step: Double): List<Double> {
        val scaledStart = start / step
        val scaledEnd = end / step
        val floorGap = scaledStart - scaledStart.toInt()
        return ContiguousSet.create(Range.closed(scaledStart.toInt(), scaledEnd.toInt()), DiscreteDomain.integers())
            .stream().map { x: Int -> (x + floorGap) * step }.collect(Collectors.toList())
    }

    fun <T : Number?> meshgrid(x: List<T>, y: List<T>): Grid<T> {
        val grid = Grid<T>()
        grid.x = IntStream.range(0, y.size).mapToObj { x }
            .collect(Collectors.toList())
        grid.y = y.stream().map { t: T ->
            IntStream.range(0, x.size).mapToObj { t }
                .collect(Collectors.toList())
        }.collect(Collectors.toList())
        return grid
    }

    class Grid<T : Number?> {
        var x: List<List<T>>? = null
        var y: List<List<T>>? = null
        fun <R> calcZ(biFunction: BiFunction<T, T, R>): List<List<R>> {
            return IntStream.range(0, x!!.size).mapToObj { i: Int ->
                IntStream.range(0, x!![i].size).mapToObj { j: Int ->
                    biFunction.apply(
                        x!![i][j], y!![i][j]
                    )
                }.collect(Collectors.toList())
            }.collect(Collectors.toList())
        }
    }


}