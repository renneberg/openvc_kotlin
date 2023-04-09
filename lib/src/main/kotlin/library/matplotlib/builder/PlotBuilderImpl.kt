package library.matplotlib.builder

import library.matplotlib.kwargs.Line2DBuilder
import library.matplotlib.kwargs.Line2DBuilderImpl

class PlotBuilderImpl : PlotBuilder {
    //   private final static Logger LOGGER = LoggerFactory.getLogger(PlotBuilderImpl.class);
    private val innerBuilder = CompositeBuilder<PlotBuilder>(this)
    private val line2DBuilder: Line2DBuilder<PlotBuilder> = Line2DBuilderImpl(innerBuilder)
    override fun add(x: List<Number>): PlotBuilder {
        return innerBuilder.addToArgs(x)
    }

    override fun add(x: List<Number>, y: List<Number>): PlotBuilder {
        innerBuilder.addToArgs(x)
        return innerBuilder.addToArgs(y)
    }

    override fun add(x: List<Number>, y: List<Number>, fmt: String): PlotBuilder {
        innerBuilder.addToArgs(x)
        innerBuilder.addToArgs(y)
        return  innerBuilder.addToArgs(fmt)
    }



    override fun linestyle(arg: String): PlotBuilder {
        return line2DBuilder.linestyle(arg)
    }

    override fun ls(arg: String): PlotBuilder {
        return line2DBuilder.ls(arg)
    }

    override fun linewidth(arg: Double): PlotBuilder {
        return line2DBuilder.linewidth(arg)
    }

    override fun lw(arg: Double): PlotBuilder {
        return line2DBuilder.lw(arg)
    }

    override fun label(arg: String): PlotBuilder {
        return line2DBuilder.label(arg)
    }

    override fun color(arg: String): PlotBuilder {
        return line2DBuilder.color(arg)
    }

    override fun build(): String {
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "plot"


}
