package library.matplotlib.kwargs

import library.matplotlib.builder.Builder

interface Line2DBuilder<T : Builder> : KwargsBuilder {
    fun linestyle(arg: String): T
    fun ls(arg: String): T
    fun linewidth(arg: Double): T
    fun lw(arg: Double): T
    fun label(arg: String): T
    fun color(arg: String): T
}
