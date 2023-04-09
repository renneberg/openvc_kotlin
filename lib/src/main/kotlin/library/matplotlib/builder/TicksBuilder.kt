package library.matplotlib.builder

import library.matplotlib.kwargs.TextArgsBuilder


/**
 * matplotlib.pyplot.xticks(ticks=None, labels=None, **kwargs)
 * matplotlib.pyplot.yticks(ticks=None, labels=None,  **kwargs)
 */
interface TicksBuilder : Builder, TextArgsBuilder<TicksBuilder> {
    fun labels(labels: List<String>): TicksBuilder
}
