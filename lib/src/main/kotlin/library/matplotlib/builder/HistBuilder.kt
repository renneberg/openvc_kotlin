package library.matplotlib.builder

import library.matplotlib.kwargs.PatchBuilder

/**
 * matplotlib.pyplot.hist(x, **kwargs)
 */
interface HistBuilder : Builder, PatchBuilder<HistBuilder> {
    enum class HistType {
        bar,
        barstacked,
        step,
        stepfilled
    }

    enum class Align {
        left,
        mid,
        right
    }

    enum class Orientation {
        horizontal,
        vertical
    }

    fun add(nums: List<Number>): HistBuilder
    fun bins(arg: Int): HistBuilder
    fun bins(nums: List<Number>): HistBuilder
    fun range(lower: Double, upper: Double): HistBuilder
    fun density(arg: Boolean): HistBuilder
    fun weights(nums: List<Number>): HistBuilder
    fun cumulative(arg: Boolean): HistBuilder
    fun bottom(arg: Double): HistBuilder
    fun bottom(nums: List<Number>): HistBuilder
    fun histtype(histType: HistType): HistBuilder
    fun align(align: Align): HistBuilder
    fun orientation(orientation: Orientation): HistBuilder
    fun rwidth(arg: Double): HistBuilder
    fun log(arg: Boolean): HistBuilder
    fun color(vararg args: String): HistBuilder
    fun stacked(arg: Boolean): HistBuilder
}
