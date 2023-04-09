package library.matplotlib

import com.google.common.base.Preconditions
import com.google.common.collect.ContiguousSet
import com.google.common.collect.DiscreteDomain
import com.google.common.collect.Range
import java.util.function.BiFunction
import java.util.stream.Collectors
import java.util.stream.IntStream

object NumpyUtils {
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
        grid.x = IntStream.range(0, y.size).mapToObj { i: Int -> x }.collect(Collectors.toList())
        grid.y = y.stream().map { t: T ->
            IntStream.range(0, x.size).mapToObj { i: Int -> t }
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
