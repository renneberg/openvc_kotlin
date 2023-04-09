package library.matplotlib.builder

interface PColorBuilder : Builder {
    /**
     * Equivalent to `pyplot.pcolor(C)`
     *
     * @param C array C
     * @return PColorBuilder for method chain
     */
    fun add(C: List<Number>): PColorBuilder

    /**
     * Equivalent to `pyplot.pcolor(X, Y, C)`
     *
     * @param X the x coordinate of the surface
     * @param Y the y coordinate of the surface
     * @param C array C
     * @return PColorBuilder for method chain
     */
    fun add(X: List<Number>, Y: List<Number>, C: List<List<Number>>): PColorBuilder

    // TODO: support V and N
    fun cmap(colorMap: String): PColorBuilder
    fun vmin(arg: Double): PColorBuilder
    fun vmax(arg: Double): PColorBuilder
    fun edgecolors(arg: String): PColorBuilder
    fun alpha(arg: Double): PColorBuilder
    val retName: String
}
