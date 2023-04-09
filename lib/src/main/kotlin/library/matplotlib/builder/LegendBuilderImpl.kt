package library.matplotlib.builder

class LegendBuilderImpl : LegendBuilder {
    private val innerBuilder = CompositeBuilder<LegendBuilder>(this)
    override fun loc(arg: Int): LegendBuilder {
        return innerBuilder.addToKwargs("loc", arg)
    }

    override fun loc(arg: String): LegendBuilder {
        return innerBuilder.addToKwargs("loc", arg)
    }

    override fun loc(x: Double, y: Double): LegendBuilder {
        return innerBuilder.addToKwargsWithoutQuoting("loc", String.format("(%d, %d)", x, y))
    }

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "legend"


}
