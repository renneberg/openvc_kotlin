package library.matplotlib

class PythonConfig private constructor(val pyenv: String?, val virtualenv: String?, val pythonBinPath: String?) {

    companion object {
        @JvmStatic
        fun systemDefaultPythonConfig(): PythonConfig {
            return PythonConfig(null, null, null)
        }

        fun pyenvConfig(pyenv: String?): PythonConfig {
            return PythonConfig(pyenv, null, null)
        }

        fun pyenvVirtualenvConfig(pyenv: String, virtualenv: String): PythonConfig {
            return PythonConfig(pyenv, virtualenv, null)
        }

        fun pythonBinPathConfig(pythonBinPath: String): PythonConfig {
            return PythonConfig(null, null, pythonBinPath)
        }
    }
}
