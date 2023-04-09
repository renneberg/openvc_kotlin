package library.matplotlib

import com.google.common.base.Strings
import com.google.common.collect.Lists
import com.google.common.io.Files
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.regex.Pattern

class PyCommand(private val pythonConfig: PythonConfig) {
    private fun buildCommandArgs(scriptPath: String): List<String> {
        val shell = StringBuilder()
        if (!Strings.isNullOrEmpty(pythonConfig.pyenv)) {
            shell.append("pyenv shell ").append(pythonConfig.pyenv).append("; ")
            if (!Strings.isNullOrEmpty(pythonConfig.virtualenv)) {
                shell.append("export PYENV_VIRTUALENV_DISABLE_PROMPT=1; ")
                shell.append("pyenv activate ").append(pythonConfig.virtualenv).append("; ")
            }
            shell.append("python ").append(scriptPath)
        }
        val com: List<String> = if (!Strings.isNullOrEmpty(pythonConfig.pyenv)) {
            Lists.newArrayList<String>(pythonConfig.pythonBinPath, scriptPath)
        } else if (shell.isNotEmpty()) {
            // -l: Use login shell
            Lists.newArrayList<String>("bash", "-l", "-c", shell.toString())
        } else {
            // system's default
            Lists.newArrayList<String>("python3", scriptPath)
        }

        // LOGGER.debug("Commands... : {}", com);
        return com
    }

    @Throws(IOException::class, PythonExecutionException::class)
    private fun command(commands: List<String>) {
        val pb = ProcessBuilder(commands)
        val process = pb.start()

        // stdout
        var br = BufferedReader(InputStreamReader(process.inputStream))
        var line = br.readLine()
        while (line != null) {
            println(line)
            line = br.readLine()
        }

        // stderr
        // TODO: have a common way with stdout
        br = BufferedReader(InputStreamReader(process.errorStream))
        val sb = StringBuilder()
        line = br.readLine()
        var hasError = false
        while (line != null) {
            sb.append(line).append('\n')
            val matcher = ERROR_PAT.matcher(line)
            if (matcher.find()) {
                hasError = true
            }
            line = br.readLine()
        }
        val msg = sb.toString()
        if (hasError) {
            //     LOGGER.error(msg);
            throw PythonExecutionException("Python execution error: $msg")
        } else {
            //     LOGGER.warn(msg);
        }
    }

    @Throws(IOException::class)
    private fun writeFile(pythonScript: String, script: File) {
        val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(script), StandardCharsets.UTF_8))
        bw.write(pythonScript)
        bw.close()
    }

    @Throws(IOException::class, PythonExecutionException::class)
    fun execute(pythonScript: String) {

        var path = File("tmp")

        val tmpDir  = if (path.exists() && path.isDirectory ) path else {path.mkdirs();path}
        tmpDir.deleteOnExit()
        val script = File(tmpDir, "exec.py")
        writeFile(pythonScript, script)
        val scriptPath = Paths.get(script.toURI()).toAbsolutePath().toString()
        command(buildCommandArgs(scriptPath))
        tmpDir.delete()
    }

    companion object {
        //   private final static Logger LOGGER = LoggerFactory.getLogger(PyCommand.class);
        private val ERROR_PAT = Pattern.compile("^.+Error:")
    }
}
