package library.matplotlib.builder

import library.matplotlib.kwargs.TextArgsBuilder
import library.matplotlib.kwargs.TextArgsBuilderImpl


class TextBuilderImpl(x: Double, y: Double, s: String?) : TextBuilder {
    private val innerBuilder = CompositeBuilder<TextBuilder>(this)

    // TODO: Add kwargs with textBuilder
    private val textBuilder: TextArgsBuilder<TextBuilder> = TextArgsBuilderImpl(innerBuilder)

    init {
        innerBuilder.addToArgs(x)
        innerBuilder.addToArgs(y)
        innerBuilder.addToArgs(s!!)
    }

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "text"


}
