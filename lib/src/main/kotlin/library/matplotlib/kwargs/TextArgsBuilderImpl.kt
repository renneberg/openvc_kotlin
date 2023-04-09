package library.matplotlib.kwargs

import library.matplotlib.builder.Builder
import library.matplotlib.builder.CompositeBuilder

class TextArgsBuilderImpl<T : Builder>(// TODO: Add Text properties as Line2DBuilderImpl (https://matplotlib.org/stable/api/text_api.html#matplotlib.text.Text)
    private val innerBuilder: CompositeBuilder<T>
) : TextArgsBuilder<T>
