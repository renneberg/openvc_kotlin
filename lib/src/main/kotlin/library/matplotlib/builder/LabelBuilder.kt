package library.matplotlib.builder

import library.matplotlib.kwargs.TextArgsBuilder

/**
 * matplotlib.pyplot.xlabel(s, *args, **kwargs)
 * matplotlib.pyplot.ylabel(s, *args, **kwargs)
 */
interface LabelBuilder : Builder, TextArgsBuilder<LabelBuilder>
