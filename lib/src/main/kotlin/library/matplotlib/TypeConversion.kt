package library.matplotlib

import java.util.stream.Collectors

enum class TypeConversion {
    INSTANCE;

    fun typeSafeList(orgList: List<*>): List<Any> {
        return orgList.stream().map { x: Any? ->
            if (x == null) {
                return@map PYTHON_NONE
            } else if (x is String) {
                return@map "\"" + x + "\""
            } else if (x is Double) {
                val v = x
                if (java.lang.Double.isInfinite(v)) {
                    return@map if (v > 0) "np.inf" else "-np.inf"
                } else if (java.lang.Double.isNaN(v)) {
                    return@map "np.nan"
                }
            } else if (x is Float) {
                val v = x
                if (java.lang.Float.isInfinite(v)) {
                    return@map if (v > 0) "np.inf" else "-np.inf"
                } else if (java.lang.Float.isNaN(v)) {
                    return@map "np.nan"
                }
            }
            x
        }.collect(Collectors.toList())
    }

    companion object {
        private const val PYTHON_NONE = "None"
    }
}
