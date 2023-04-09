package library.matplotlib.builder

import library.matplotlib.kwargs.TextArgsBuilder


/**
 * matplotlib.pyplot.xscale(scale, **kwargs)
 * matplotlib.pyplot.yscale(scale, **kwargs)
 */
interface ScaleBuilder : Builder, TextArgsBuilder<ScaleBuilder> {
    enum class Scale {
        linear,
        log,
        logit,
        symlog
    }
}
