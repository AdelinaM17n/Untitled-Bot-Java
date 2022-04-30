package bot.lib.commandhandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ChatCommandContainer {
    private final String name;
    private final int nonOptionalArgCount;
    private final Field[] orderedFieldList;
    private final Method executionMethod;
    private final Class<?> argsClass;
    private final Class<?>[] argsFieldTypeList;

    public ChatCommandContainer(String name, int nonOptionalArgCount, Field[] orderedFieldList, Method executionMethod, Class<?> argsClass, Class<?>[] argsFieldTypeList) {
        this.name = name;
        this.nonOptionalArgCount = nonOptionalArgCount;
        this.orderedFieldList = orderedFieldList;
        this.executionMethod = executionMethod;
        this.argsClass = argsClass;
        this.argsFieldTypeList = argsFieldTypeList;
    }
}
