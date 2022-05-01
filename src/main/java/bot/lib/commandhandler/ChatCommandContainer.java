package bot.lib.commandhandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("all")
public class ChatCommandContainer {
    private final String name;
    private final int nonOptionalArgCount;
    private final Field[] orderedFieldList;
    private final Method executionMethod;
    private final Class<?> argsClass;
    private final Class<?>[] argsFieldTypeList;
    private final Extension extensionInstance;

    public ChatCommandContainer(String name, int nonOptionalArgCount, Field[] orderedFieldList, Method executionMethod,
            Class<?> argsClass, Class<?>[] argsFieldTypeList,Extension extensionInstance) {
        this.name = name;
        this.nonOptionalArgCount = nonOptionalArgCount;
        this.orderedFieldList = orderedFieldList;
        this.executionMethod = executionMethod;
        this.argsClass = argsClass;
        this.argsFieldTypeList = argsFieldTypeList;
        this.extensionInstance = extensionInstance;
    }

    public int getNonOptionalArgCount(){
        return nonOptionalArgCount;
    }

    public boolean hasNonOptionalArgs(){
        return nonOptionalArgCount > 0;
    }

    public Field[] getOrderedFieldList(){
        return Arrays.copyOf(orderedFieldList, orderedFieldList.length);
    }

    public Class<?>[] getArgsFieldTypeList(){
        return Arrays.copyOf(argsFieldTypeList,argsFieldTypeList.length);
    }

    public Method getExecutionMethod(){
        return executionMethod;
    }

    public Class<?> getArgsClass(){
        return argsClass;
    }

    public String getName(){
        return name;
    }

    public Extension getExtensionInstance(){
        return extensionInstance;
    }
}
