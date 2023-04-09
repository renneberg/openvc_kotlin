package library.matplotlib.builder

class CLabelBuilderImpl(contour: ContourBuilder) : CLabelBuilder {
    private val innerBuilder = CompositeBuilder<CLabelBuilder>(this)

    init {
        innerBuilder.addToArgsWithoutQuoting( contour.retName)
    }

    override fun fontsize(arg: String): CLabelBuilder {
        return innerBuilder.addToKwargs("fontsize", arg)
    }

    override fun fontsize(arg: Double): CLabelBuilder {
        return innerBuilder.addToKwargs("fontsize", arg)
    }

    override fun inline(arg: Boolean): CLabelBuilder {
        return innerBuilder.addToKwargs("inline", arg)
    }

    override fun inlineSpacing(arg: Double): CLabelBuilder {
        return innerBuilder.addToKwargs("inline_spacing", arg)
    }

    override fun fmt(arg: String): CLabelBuilder {
        return  innerBuilder.addToKwargs("fmt", arg)
    }

    override fun manual(arg: Boolean): CLabelBuilder {
        return innerBuilder.addToKwargs("manual", arg)
    }

    override fun rightsideUp(arg: Boolean): CLabelBuilder {
        return innerBuilder.addToKwargs("rightside_up", arg)
    }

    override fun useClabeltext(arg: Boolean): CLabelBuilder {
        return innerBuilder.addToKwargs("use_clabeltext", arg)
    }

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "clabel"


}
