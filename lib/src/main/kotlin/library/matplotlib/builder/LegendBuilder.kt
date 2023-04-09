package library.matplotlib.builder

/**
 * matplotlib.pyplot.legend(*args, **kwargs)
 */
interface LegendBuilder : Builder {
    fun loc(arg: Int): LegendBuilder
    fun loc(arg: String): LegendBuilder
    fun loc(x: Double, y: Double): LegendBuilder
}
