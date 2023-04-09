package library.matplotlib.builder

class SaveFigBuilderImpl(fname: String) : SaveFigBuilder {
    private val innerBuilder = CompositeBuilder<SaveFigBuilder>(this)

    init {
        innerBuilder.addToArgs(fname)
    }

    override fun dpi(arg: Double): SaveFigBuilder {
        return innerBuilder.addToKwargs("dpi", arg)
    }

    override fun facecolor(arg: String): SaveFigBuilder {
        return innerBuilder.addToKwargs("facecolor", arg)
    }

    override fun orientation(orientation: SaveFigBuilder.Orientation): SaveFigBuilder {
        return innerBuilder.addToKwargs("orientation", orientation.toString())
    }

    override fun papertype(arg: String): SaveFigBuilder {
        return innerBuilder.addToKwargs("papertype", arg)
    }

    override fun format(arg: String): SaveFigBuilder {
        return innerBuilder.addToKwargs("format", arg)
    }

    override fun transparent(arg: Boolean): SaveFigBuilder {
        return innerBuilder.addToKwargs("transparent", arg)
    }

    override fun frameon(arg: Boolean): SaveFigBuilder {
        return innerBuilder.addToKwargs("frameon", arg)
    }

    override fun bboxInches(arg: Double): SaveFigBuilder {
        return innerBuilder.addToKwargs("bboxInches", arg)
    }

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() =  "savefig"


}
