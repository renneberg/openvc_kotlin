package library.matplotlib.kwargs

import library.matplotlib.builder.Builder
import library.matplotlib.builder.CompositeBuilder

class Line2DBuilderImpl<T : Builder>(private val innerBuilder: CompositeBuilder<T>) : Line2DBuilder<T> {
    override fun linestyle(arg: String): T {
        return ls(arg)
    }

    override fun ls(arg: String): T {
        return innerBuilder.addToKwargs("ls", arg)
    }

    override fun linewidth(arg: Double): T {
        return lw(arg)
    }

    override fun lw(arg: Double): T {
        return innerBuilder.addToKwargs("lw", arg)
    }

    override fun label(arg: String): T {
        return innerBuilder.addToKwargs("label", arg)
    }

    override fun color(arg: String): T {
        return innerBuilder.addToKwargs("color", arg)
    }
}
