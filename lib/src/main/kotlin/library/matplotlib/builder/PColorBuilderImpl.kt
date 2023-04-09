package library.matplotlib.builder

class PColorBuilderImpl : PColorBuilder {
    private val innerBuilder = CompositeBuilder<PColorBuilder>(this)
    override fun add(C: List<Number>): PColorBuilder {
        return innerBuilder.addToArgs(C)
    }

    override fun add(X: List<Number>, Y: List<Number>, C: List<List<Number>>): PColorBuilder {
        innerBuilder.addToArgs(X)
        innerBuilder.addToArgs(Y)
        return innerBuilder.add2DimListToArgs(C)
    }

    override fun cmap(colorMap: String): PColorBuilder {
        return innerBuilder.addToKwargsWithoutQuoting("cmap", colorMap)
    }

    override fun vmin(arg: Double): PColorBuilder {
        return innerBuilder.addToKwargs("vmin", arg)
    }

    override fun vmax(arg: Double): PColorBuilder {
        return innerBuilder.addToKwargs("vmax", arg)
    }

    override fun edgecolors(arg: String): PColorBuilder {
        return innerBuilder.addToKwargs("edgecolors", arg)
    }

    override fun alpha(arg: Double): PColorBuilder {
        return innerBuilder.addToKwargs("alpha", arg)
    }

    override val retName: String
        get() = innerBuilder.retName

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "pcolor"


}
