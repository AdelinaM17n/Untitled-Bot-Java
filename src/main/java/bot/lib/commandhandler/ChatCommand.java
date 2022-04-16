package bot.lib.commandhandler;

import bot.lib.commandhandler.annotation.ArgInnerClass;
import bot.lib.commandhandler.annotation.CommandArg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class ChatCommand {
    private final String name;
    private final int nonOptionalArgCount;
    private final Field[] orderedFieldList;
    private final Method executionMethod;
    private final Class<?> argsClass;
    private final Class<?>[] argsFieldTypeList;

    public ChatCommand(String name){
        this.name = name;
        argsClass = setArgsObject();
        orderedFieldList = setOrderedFieldList(argsClass);
        argsFieldTypeList = setArgsFieldTypeList(orderedFieldList);
        nonOptionalArgCount = setNonOptionalArgCount(argsClass);
        executionMethod = setExecutionMethod();
    }

    //public abstract void run(HashMap<String,Object> args, Message message, Guild guild);
    private Class<?>[] setArgsFieldTypeList(Field[] orderedFieldList){
        var filedTypeArray = new Class<?>[orderedFieldList.length+1];
        filedTypeArray[0] = this.getClass();
        for(int i =1; i < orderedFieldList.length; i++){
            filedTypeArray[i] = orderedFieldList[i-1].getType();
        }
        return filedTypeArray;
    }

    private Class<?> setArgsObject(){
        var innerclass = Arrays.stream(this.getClass().getDeclaredClasses()).filter(aClass -> aClass.isAnnotationPresent(ArgInnerClass.class)).findFirst();
        return innerclass.orElse(null);
    }

    private Method setExecutionMethod(){
        var method = Arrays.stream(this.getClass().getDeclaredMethods()).filter(method1 -> method1.getName().equals("run")).findFirst();
        return method.orElse(null);
    }

    private int setNonOptionalArgCount(Class<?> clazz){
        var fieldList =  new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        fieldList.removeIf(field -> !field.isAnnotationPresent(CommandArg.class) || (field.isAnnotationPresent(CommandArg.class) && field.getAnnotation(CommandArg.class).optional()));
        return fieldList.size();
    }
    private Field[] setOrderedFieldList(Class<?> clazz){
        var fieldList = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        fieldList.removeIf(field -> !field.isAnnotationPresent(CommandArg.class));
        Field[] orderedList = new Field[fieldList.size()];
        for(Field field : fieldList){ orderedList[field.getAnnotation(CommandArg.class).index()] = field; }
        return orderedList;
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
}
