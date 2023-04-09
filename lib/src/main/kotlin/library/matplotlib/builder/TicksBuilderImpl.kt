package library.matplotlib.builder

import library.matplotlib.kwargs.TextArgsBuilder
import library.matplotlib.kwargs.TextArgsBuilderImpl


class TicksBuilderImpl(ticks: List<Number>, override val methodName: String) : TicksBuilder {
    private val innerBuilder = CompositeBuilder<TicksBuilder>(this)

    // TODO: Add kwargs with textBuilder
    private val textBuilder: TextArgsBuilder<TicksBuilder> = TextArgsBuilderImpl(innerBuilder)
    private val labels: List<String>? = null

    init {
        // Add labels without ticks causes an error, that's why made ticks as mandatory parameter
        // matplotlib.units.ConversionError: Failed to convert value(s) to axis units: ['a', 'b']
        innerBuilder.addToArgs(ticks)
    }

    override fun labels(labels: List<String>): TicksBuilder {
        innerBuilder.addToArgs(labels)
        return this
    }

    override fun build(): String {
        return innerBuilder.build()
    }



    companion object {
        fun xTicksBuilder(ticks: List<Number>): TicksBuilderImpl {
            return TicksBuilderImpl(ticks, "xticks")
        }

        fun yTicksBuilder(ticks: List<Number>): TicksBuilderImpl {
            return TicksBuilderImpl(ticks, "yticks")
        }
    }
}
