package library.matplotlib.builder

import com.google.common.base.Joiner
import com.google.common.base.Preconditions
import library.matplotlib.builder.HistBuilder.Align
import library.matplotlib.builder.HistBuilder.HistType
import library.matplotlib.kwargs.PatchBuilder
import library.matplotlib.kwargs.PatchBuilderImpl
import java.util.*

class HistBuilderImpl : HistBuilder {
    private val innerBuilder = CompositeBuilder<HistBuilder>(this)
    private val patchBuilder: PatchBuilder<HistBuilder> = PatchBuilderImpl(innerBuilder)
    private val xList: MutableList<List<Number?>?> = LinkedList()
    override fun add(nums: List<Number>): HistBuilder {
        xList.add(nums)
        return this
    }

    override fun bins(arg: Int): HistBuilder {
        return innerBuilder.addToKwargs("bins", arg)
    }

    override fun bins(nums: List<Number>): HistBuilder {
        return innerBuilder.addToKwargs("bins", nums)
    }

    override fun range(lower: Double, upper: Double): HistBuilder {
        return innerBuilder.addToKwargsWithoutQuoting("range", String.format("(%f, %f)", lower, upper))
    }

    override fun density(arg: Boolean): HistBuilder {
        return innerBuilder.addToKwargs("density", arg)
    }

    override fun weights(nums: List<Number>): HistBuilder {
        return innerBuilder.addToKwargs("weights", nums)
    }

    override fun cumulative(arg: Boolean): HistBuilder {
        return innerBuilder.addToKwargs("cumulative", arg)
    }

    override fun bottom(arg: Double): HistBuilder {
        return innerBuilder.addToKwargs("bottom", arg)
    }

    override fun bottom(nums: List<Number>): HistBuilder {
        return innerBuilder.addToKwargs("bottom", nums)
    }

    override fun histtype(histType: HistType): HistBuilder {
        return innerBuilder.addToKwargs("histtype", histType.toString())
    }

    override fun align(align: Align): HistBuilder{
        return innerBuilder.addToKwargs("align", align.name)
    }

    override fun orientation(orientation: HistBuilder.Orientation): HistBuilder {
        return innerBuilder.addToKwargs("orientation", orientation.name)
    }

    override fun rwidth(arg: Double): HistBuilder {
        return innerBuilder.addToKwargs("rwidth", arg)
    }

    override fun log(arg: Boolean): HistBuilder {
        return innerBuilder.addToKwargs("log", arg)
    }

    override fun color(vararg args: String): HistBuilder {
        Preconditions.checkArgument(args.size > 0, ".color() needs to have at least one argument.")
        return innerBuilder.addToKwargsWithoutQuoting("color", "[\"" + Joiner.on("\", \"").join(args) + "\"]")
    }



    override fun stacked(arg: Boolean): HistBuilder {
        return innerBuilder.addToKwargs("stacked", arg)
    }

    override fun linestyle(arg: String): HistBuilder {
        return patchBuilder.linestyle(arg)
    }

    override fun ls(arg: String): HistBuilder {
        return patchBuilder.ls(arg)
    }

    override fun linewidth(arg: Double): HistBuilder {
        return patchBuilder.linewidth(arg)
    }

    override fun lw(arg: Double): HistBuilder {
        return patchBuilder.lw(arg)
    }



    override fun label(arg: String): HistBuilder {
        return patchBuilder.label(arg)
    }

    override fun build(): String {
        Preconditions.checkArgument(xList.size > 0, ".add() is needed to be called at least once.")
        innerBuilder.addToArgsWithoutQuoting(xList.toString())
        return innerBuilder.build()
    }

    override val methodName: String
        get() = "hist"




}
