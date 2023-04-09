package library.matplotlib.builder

import library.matplotlib.kwargs.Line2DBuilder

/**
 * matplotlib.pyplot.plot(*args, **kwargs)
 */
interface PlotBuilder : Builder, Line2DBuilder<PlotBuilder> {
    fun add(nums: List<Number>): PlotBuilder
    fun add(x: List<Number>, y: List<Number>): PlotBuilder
    fun add(x: List<Number>, y: List<Number>, fmt: String): PlotBuilder
}
