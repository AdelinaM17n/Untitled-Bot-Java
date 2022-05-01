package bot.lib.commandhandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

//@SuppressWarnings("all")
public record ChatCommandContainer(
        int nonOptionalArgCount,
        Field[] orderedFieldList,
        Method executionMethod,
        Class<?> argsClass,
        Class<?>[] argsFieldTypeList,
        Extension extensionInstance
){
    public boolean hasNonOptionalArgs() {return nonOptionalArgCount > 0;}
    public Field[] orderedFieldList() {return Arrays.copyOf(orderedFieldList, orderedFieldList.length);}
    public Class<?>[] argsFieldTypeList() {return Arrays.copyOf(argsFieldTypeList, argsFieldTypeList.length);}
}
