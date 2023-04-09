package library.matplotlib.builder

class LabelBuilderImpl(label: String?, override val methodName: String) : LabelBuilder {
    private val innerBuilder = CompositeBuilder<LabelBuilder>(this)

    init {
        innerBuilder.addToArgs(label!!)
    }

    override fun build(): String {
        return innerBuilder.build()
    }



    companion object {
        @JvmStatic
        fun xLabelBuilder(label: String): LabelBuilderImpl {
            return LabelBuilderImpl(label, "xlabel")
        }

        @JvmStatic
        fun yLabelBuilder(label: String): LabelBuilderImpl {
            return LabelBuilderImpl(label, "ylabel")
        }
    }
}
