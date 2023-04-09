package library.matplotlib.builder

interface Builder {
    fun build(): String
    val methodName: String
}
