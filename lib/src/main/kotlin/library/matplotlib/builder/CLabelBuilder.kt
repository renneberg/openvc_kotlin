package library.matplotlib.builder

interface CLabelBuilder : Builder {
    fun fontsize(arg: String): CLabelBuilder?
    fun fontsize(arg: Double): CLabelBuilder?
    fun inline(arg: Boolean): CLabelBuilder?
    fun inlineSpacing(arg: Double): CLabelBuilder?
    fun fmt(arg: String): CLabelBuilder?
    fun manual(arg: Boolean): CLabelBuilder?
    fun rightsideUp(arg: Boolean): CLabelBuilder?
    fun useClabeltext(arg: Boolean): CLabelBuilder?
}
