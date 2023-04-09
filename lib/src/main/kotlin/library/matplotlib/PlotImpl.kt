package library.matplotlib

import com.google.common.annotations.VisibleForTesting
import com.google.common.base.Joiner
import library.matplotlib.PythonConfig.Companion.systemDefaultPythonConfig
import library.matplotlib.builder.*
import library.matplotlib.builder.LabelBuilderImpl.Companion.xLabelBuilder
import library.matplotlib.builder.LabelBuilderImpl.Companion.yLabelBuilder
import library.matplotlib.builder.ScaleBuilderImpl.Companion.xScaleBuilder
import library.matplotlib.builder.ScaleBuilderImpl.Companion.yScaleBuilder
import java.io.IOException
import java.util.*
import java.util.function.Consumer

class PlotImpl internal constructor(private val pythonConfig: PythonConfig, private val dryRun: Boolean) : Plot {
    @VisibleForTesting
    var registeredBuilders: MutableList<Builder> = LinkedList()
    private val showBuilders: List<Builder> = LinkedList()

    @VisibleForTesting
    internal constructor(dryRun: Boolean) : this(systemDefaultPythonConfig(), dryRun)

    override fun legend(): LegendBuilder {
        val builder: LegendBuilder = LegendBuilderImpl()
        registeredBuilders.add(builder)
        return builder
    }

    override fun figure(windowTitle: String) {
        registeredBuilders.add(ArgsBuilderImpl("figure", windowTitle))
    }

    override fun title(title: String) {
        registeredBuilders.add(ArgsBuilderImpl("title", title))
    }

    override fun xlabel(label: String): LabelBuilder {
        val builder: LabelBuilder = xLabelBuilder(label)
        registeredBuilders.add(builder)
        return builder
    }

    override fun ylabel(label: String): LabelBuilder {
        val builder: LabelBuilder = yLabelBuilder(label)
        registeredBuilders.add(builder)
        return builder
    }

    override fun xscale(scale: ScaleBuilder.Scale): ScaleBuilder {
        val builder: ScaleBuilder = xScaleBuilder(scale)
        registeredBuilders.add(builder)
        return builder
    }

    override fun yscale(scale: ScaleBuilder.Scale): ScaleBuilder {
        val builder: ScaleBuilder = yScaleBuilder(scale)
        registeredBuilders.add(builder)
        return builder
    }

    override fun xlim(xmin: Number, xmax: Number) {
        registeredBuilders.add(ArgsBuilderImpl("xlim", xmin, xmax))
    }

    override fun ylim(ymin: Number, ymax: Number) {
        registeredBuilders.add(ArgsBuilderImpl("ylim", ymin, ymax))
    }

    override fun xticks(ticks: List<Number>): TicksBuilder {
        val builder: TicksBuilder = TicksBuilderImpl.xTicksBuilder(ticks)
        registeredBuilders.add(builder)
        return builder
    }

    override fun yticks(ticks: List<Number>): TicksBuilder {
        val builder: TicksBuilder = TicksBuilderImpl.yTicksBuilder(ticks)
        registeredBuilders.add(builder)
        return builder
    }

    override fun text(x: Double, y: Double, s: String): TextBuilder {
        val builder: TextBuilder = TextBuilderImpl(x, y, s)
        registeredBuilders.add(builder)
        return builder
    }

    override fun plot(): PlotBuilder {
        val builder: PlotBuilder = PlotBuilderImpl()
        registeredBuilders.add(builder)
        return builder
    }

    override fun contour(): ContourBuilder {
        val builder: ContourBuilder = ContourBuilderImpl()
        registeredBuilders.add(builder)
        return builder
    }

    override fun pcolor(): PColorBuilder {
        val builder: PColorBuilder = PColorBuilderImpl()
        registeredBuilders.add(builder)
        return builder
    }

    override fun hist(): HistBuilder {
        val builder: HistBuilder = HistBuilderImpl()
        registeredBuilders.add(builder)
        return builder
    }

    override fun clabel(contour: ContourBuilder): CLabelBuilder {
        val builder: CLabelBuilder = CLabelBuilderImpl(contour)
        registeredBuilders.add(builder)
        return builder
    }

    override fun savefig(fname: String): SaveFigBuilder {
        val builder: SaveFigBuilder = SaveFigBuilderImpl(fname)
        registeredBuilders.add(builder)
        return builder
    }

    override fun subplot(nrows: Int, ncols: Int, index: Int): SubplotBuilder {
        val builder: SubplotBuilder = SubplotBuilderImpl(nrows, ncols, index)
        registeredBuilders.add(builder)
        return builder
    }

    override fun close() {
        registeredBuilders.add(ArgsBuilderImpl("close"))
    }

    override fun close(name: String) {
        registeredBuilders.add(ArgsBuilderImpl("close", name))
    }

    @Throws(IOException::class, PythonExecutionException::class)
    override fun executeSilently() {
        val scriptLines: MutableList<String> = LinkedList()
        scriptLines.add("import numpy as np")
        scriptLines.add("import matplotlib as mpl")
        scriptLines.add("mpl.use('Agg')")
        scriptLines.add("import matplotlib.pyplot as plt")
        registeredBuilders.forEach(Consumer { b: Builder -> scriptLines.add(b.build()) })
        showBuilders.forEach(Consumer { b: Builder -> scriptLines.add(b.build()) })
        val command = PyCommand(pythonConfig)
        command.execute(Joiner.on('\n').join(scriptLines))
    }

    /**
     * matplotlib.pyplot.show(*args, **kw)
     */
    @Throws(IOException::class, PythonExecutionException::class)
    override fun show() {
        val scriptLines: MutableList<String> = LinkedList()
        scriptLines.add("import numpy as np")
        if (dryRun) {
            // No need DISPLAY for test run
            scriptLines.add("import matplotlib as mpl")
            scriptLines.add("mpl.use('Agg')")
        }
        scriptLines.add("import matplotlib.pyplot as plt")
        registeredBuilders.forEach(Consumer { b: Builder -> scriptLines.add(b.build()) })

        // show
        if (!dryRun) {
            scriptLines.add("plt.show()")
        }
        val command = PyCommand(pythonConfig)
        command.execute(Joiner.on('\n').join(scriptLines))

        // After showing, registered plot is cleared
        registeredBuilders.clear()
    }
}
