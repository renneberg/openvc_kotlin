package library.matplotlib.builder

import com.google.common.base.Joiner
import java.util.*


class ArgsBuilderImpl : Builder {
    private val key: String
    var args: MutableList<Any> = LinkedList<Any>()

    constructor(key: String) {
        this.key = key
    }

    constructor(key: String, arg: String) {
        this.key = key
        addStringToArgs(arg)
    }

    constructor(key: String, arg1: String, arg2: String) {
        this.key = key
        addStringToArgs(arg1)
        addStringToArgs(arg2)
    }

    constructor(key: String, arg: Number) {
        this.key = key
        addStringToArgs(arg)
    }

    constructor(key: String, arg1: Number, arg2: Number) {
        this.key = key
        addStringToArgs(arg1)
        addStringToArgs(arg2)
    }

    private fun addStringToArgs(v: String): ArgsBuilderImpl {
        // TODO: Do it with StringBuilder on join
        args.add("\"" + v + "\"")
        return this
    }

    private fun addStringToArgs(v: Number): ArgsBuilderImpl {
        args.add(v)
        return this
    }

    override fun build(): String {
        val sb = StringBuilder()
        sb.append("plt.")
        sb.append(key)
        sb.append('(')
        Joiner.on(',').appendTo(sb, args)
        sb.append(')')
        val str = sb.toString()
      //  LOGGER.debug(".plot command: {}", str)
        return str
    }

    override val methodName: String
        get() = key


    companion object {
      //  private val LOGGER: Logger = LoggerFactory.getLogger(ArgsBuilderImpl::class.java)
    }
}
