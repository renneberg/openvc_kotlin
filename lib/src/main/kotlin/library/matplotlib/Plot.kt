package library.matplotlib

import library.matplotlib.builder.*
import java.io.IOException

interface Plot {
    fun legend(): LegendBuilder

    /**
     * This renews a figure. Make sure that this has to be put before adding plots.
     * Unless that, a new figure window will be open.
     */
    fun figure(windowTitle: String)
    fun title(title: String)
    fun xlabel(label: String): LabelBuilder
    fun ylabel(label: String): LabelBuilder
    fun xscale(scale: ScaleBuilder.Scale): ScaleBuilder
    fun yscale(scale: ScaleBuilder.Scale): ScaleBuilder
    fun xlim(xmin: Number, xmax: Number)
    fun ylim(ymin: Number, ymax: Number)
    fun xticks(ticks: List<Number>): TicksBuilder
    fun yticks(ticks: List<Number>): TicksBuilder
    fun text(x: Double, y: Double, s: String): TextBuilder
    fun plot(): PlotBuilder
    fun contour(): ContourBuilder
    fun pcolor(): PColorBuilder
    fun hist(): HistBuilder
    fun clabel(contour: ContourBuilder): CLabelBuilder
    fun savefig(fname: String): SaveFigBuilder
    fun subplot(nrows: Int, ncols: Int, index: Int): SubplotBuilder

    /**
     * Close a figure window.
     */
    fun close()

    /**
     * Close a figure window with name label.
     */
    fun close(name: String)

    /**
     * Silently execute Python script until here by builders.
     * It is mostly useful to execute `plt.savefig()` without showing by window.
     */
    @Throws(IOException::class, PythonExecutionException::class)
    fun executeSilently()

    /**
     * matplotlib.pyplot.show(*args, **kw)
     */
    @Throws(IOException::class, PythonExecutionException::class)
    fun show()

    companion object {
        fun create(): Plot {
            return PlotImpl(PythonConfig.systemDefaultPythonConfig(), false)
        }

        fun create(pythonConfig: PythonConfig): Plot {
            return PlotImpl(pythonConfig, false)
        }
    }
}
