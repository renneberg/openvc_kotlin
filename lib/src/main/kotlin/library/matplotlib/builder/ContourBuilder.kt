package library.matplotlib.builder

interface ContourBuilder : Builder {
    /**
     * Equivalent to `pyplot.contour(Z)`
     *
     * @param Z array Z
     * @return ContourBuilder for method chain
     */
    fun add(Z: List<Number>): ContourBuilder?

    /**
     * Equivalent to `pyplot.contour(X, Y, Z)`
     *
     * @param X the x coordinate of the surface
     * @param Y the y coordinate of the surface
     * @param Z array Z
     * @return ContourBuilder for method chain
     */
    fun add(X: List<Number>, Y: List<Number>, Z: List<List<Number>>): ContourBuilder

    // TODO: support V and N
    fun colors(arg: String): ContourBuilder
    fun vmin(arg: Double): ContourBuilder
    fun vmax(arg: Double): ContourBuilder
    fun alpha(arg: Double): ContourBuilder
    fun levels(arg: List<Number>): ContourBuilder
    val retName: String
}
