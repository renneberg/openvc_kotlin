package library.matplotlib.builder

class SubplotBuilderImpl(nrows: Int, ncols: Int, index: Int) : SubplotBuilder {
    private val innerBuilder = CompositeBuilder<SubplotBuilder>(this)

    init {
        innerBuilder.addToArgs(nrows)
        innerBuilder.addToArgs(ncols)
        innerBuilder.addToArgs(index)
    }

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "subplot"


}
