package bot.lib.commandhandler;

import net.dv8tion.jda.api.Permission;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

//@SuppressWarnings("all")
public record ChatCommandContainer(
        int nonOptionalArgCount,
        Field[] orderedFieldList,
        Method executionMethod,
        Class<?> argsClass,
        //Class<?>[] argsFieldTypeList,
        Permission[] requiredPerms,
        Extension extensionInstance
){
    public boolean hasNonOptionalArgs() {return nonOptionalArgCount > 0;}
    @Override public Field[] orderedFieldList() {return Arrays.copyOf(orderedFieldList, orderedFieldList.length);}
    //@Override public Class<?>[] argsFieldTypeList() {return Arrays.copyOf(argsFieldTypeList, argsFieldTypeList.length);}
    @Override public Permission[] requiredPerms(){return Arrays.copyOf(requiredPerms,requiredPerms.length);}
}
