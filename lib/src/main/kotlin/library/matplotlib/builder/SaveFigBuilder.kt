package library.matplotlib.builder

/**
 * matplotlib.pyplot.savefig(fname, **kwargs)
 */
interface SaveFigBuilder : Builder {
    enum class Orientation {
        horizontal,
        vertical
    }

    fun dpi(arg: Double): SaveFigBuilder
    fun facecolor(arg: String): SaveFigBuilder
    fun orientation(orientation: Orientation): SaveFigBuilder
    fun papertype(arg: String): SaveFigBuilder
    fun format(arg: String): SaveFigBuilder
    fun transparent(arg: Boolean): SaveFigBuilder
    fun frameon(arg: Boolean): SaveFigBuilder
    fun bboxInches(arg: Double): SaveFigBuilder // `bbox_extra_artists` is not supported yet
}
