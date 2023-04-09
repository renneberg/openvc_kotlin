package library.matplotlib.builder

class ContourBuilderImpl : ContourBuilder {
    private val innerBuilder = CompositeBuilder<ContourBuilder>(this)
    override fun add(Z: List<Number>): ContourBuilder? {
        return innerBuilder.addToArgs(Z)
    }

    override fun add(X: List<Number>, Y: List<Number>, Z: List<List<Number>>): ContourBuilder {
        innerBuilder.addToArgs(X)
        innerBuilder.addToArgs(Y)
        return innerBuilder.add2DimListToArgs(Z)
    }

    override fun colors(arg: String): ContourBuilder {
        return innerBuilder.addToKwargs("colors", arg)
    }

    override fun vmin(arg: Double): ContourBuilder {
        return innerBuilder.addToKwargs("vmin", arg)
    }

    override fun vmax(arg: Double): ContourBuilder {
        return innerBuilder.addToKwargs("vmax", arg)
    }

    override fun alpha(arg: Double): ContourBuilder {
        return innerBuilder.addToKwargs("alpha", arg)
    }

    override fun levels(arg: List<Number>): ContourBuilder {
        return innerBuilder.addToKwargs("levels", arg)
    }

    override val retName: String
        get() = innerBuilder.retName

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "contour"


}
