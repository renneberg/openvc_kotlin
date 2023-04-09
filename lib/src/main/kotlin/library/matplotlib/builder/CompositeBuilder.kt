package library.matplotlib.builder

import com.google.common.base.Joiner
import library.matplotlib.TypeConversion
import java.util.*
import java.util.stream.Collectors

/**
 * [CompositeBuilder] handles positional arguments and keyword arguments to methods
 * by [.build] call on behalf of the ownerBuilder with a common way.
 *
 * @param <T> Owner builder class
</T> */
class CompositeBuilder<T : Builder?>(private val ownerBuilder: T) : Builder {
    //  private final static Logger LOGGER = LoggerFactory.getLogger(CompositeBuilder.class);
    private val args: MutableList<Any> = LinkedList()
    private val kwargs: MutableMap<String?, Any?> = HashMap()
    private var beforeMethodOutput: String? = null
    private var afterMethodOutput: String? = null

    // get unique return value
    @JvmField
    val retName = "ret_" + UUID.randomUUID().toString().replace('-', '_')

    private fun wrapWithNdArray(listAsStr: String): String {
        // Change all the array_like arguments from python list to np.array because .shape is called in pcolor
        return "np.array($listAsStr)"
    }

    fun addToArgs(objs: List<*>?): T {
        objs?.let { TypeConversion.INSTANCE.typeSafeList(it).toString() }?.let { wrapWithNdArray(it) }
            ?.let { args.add(it) }
        return ownerBuilder
    }

    fun add2DimListToArgs(numbers: List<List<Number?>?>): T {
        args.add(wrapWithNdArray(
            numbers.stream().map { orgList: List<*>? ->
                if (orgList != null) {
                    TypeConversion.INSTANCE.typeSafeList(orgList)
                }
            }
                .collect(Collectors.toList()).toString()
        ))
        return ownerBuilder
    }

    fun addToArgs(v: String): T {
        // TODO: Do it with StringBuilder on join
        args.add("\"" + v + "\"")
        return ownerBuilder
    }

    fun addToArgsWithoutQuoting(v: String): T {
        args.add(v)
        return ownerBuilder
    }

    fun addToArgs(n: Number): T {
        args.add(n)
        return ownerBuilder
    }

    fun addToKwargs(k: String?, v: String): T {
        // TODO: Do it with StringBuilder on join
        kwargs[k] = "\"" + v + "\""
        return ownerBuilder
    }

    fun addToKwargsWithoutQuoting(k: String?, v: String?): T {
        kwargs[k] = v
        return ownerBuilder
    }

    fun addToKwargs(k: String?, n: Number?): T {
        kwargs[k] = n
        return ownerBuilder
    }

    fun addToKwargs(k: String?, v: List<Number?>?): T {
        kwargs[k] = v
        return ownerBuilder
    }

    fun addToKwargs(k: String?, v: Boolean): T {
        kwargs[k] = if (v) "True" else "False"
        return ownerBuilder
    }

    fun beforeMethodOutput(arg: String?) {
        beforeMethodOutput = arg
    }

    fun afterMethodOutput(arg: String?) {
        afterMethodOutput = arg
    }

    override fun build(): String {
        val sb = StringBuilder()
        if (beforeMethodOutput != null) {
            sb.append(beforeMethodOutput).append('\n')
        }

        // retName
        sb.append(retName).append(" = ")
        sb.append("plt.")
        sb.append(ownerBuilder!!.methodName)
        sb.append("(")

        // Args
        // TODO: type conversion
        Joiner.on(',').appendTo(sb, args)

        // Kwargs
        if (!kwargs.isEmpty()) {
            if (!args.isEmpty()) {
                sb.append(',')
            }
            Joiner.on(',').withKeyValueSeparator("=").appendTo(sb, kwargs)
        }
        sb.append(")")
        if (afterMethodOutput != null) {
            sb.append('\n').append(afterMethodOutput)
        }
        //  LOGGER.debug(".plot command: {}", str);
        return sb.toString()
    }

    override val methodName: String
        get() =  throw UnsupportedOperationException("CompositeBuilder doesn't have any real method.")


}
