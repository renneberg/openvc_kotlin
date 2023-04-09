package library.matplotlib.builder

class ScaleBuilderImpl private constructor(scale: ScaleBuilder.Scale, override val methodName: String) : ScaleBuilder {
    private val innerBuilder = CompositeBuilder<ScaleBuilder>(this)

    init {
        innerBuilder.addToArgs(scale.name)
    }

    override fun build(): String {
        return innerBuilder.build()
    }



    companion object {
        @JvmStatic
        fun xScaleBuilder(scale: ScaleBuilder.Scale): ScaleBuilderImpl {
            return ScaleBuilderImpl(scale, "xscale")
        }

        @JvmStatic
        fun yScaleBuilder(scale: ScaleBuilder.Scale): ScaleBuilderImpl {
            return ScaleBuilderImpl(scale, "yscale")
        }
    }
}
