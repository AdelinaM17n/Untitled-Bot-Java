package bot.lib.commandhandler;

import bot.UntitledBot;
import bot.lib.commandhandler.annotation.ArgField;
import bot.lib.commandhandler.annotation.ChatCommand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Extension {
    //private Stream<Method> commandMethods = Arrays.stream(this.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ChatCommand.class));
    // TODO
    //  MAKE A FUCKING INIT METHOD THAT WILL CREATE A COMMAND CONTAINER AND STORE IT IN THE COMMAND MAP
    public void init(){
        var methods = Arrays.stream(this.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ChatCommand.class));
        methods.forEach(method -> {
            var annotation = method.getAnnotation(ChatCommand.class);
            var orderedFieldList = findOrderedFieldList(annotation.argsClass());
            var typeList = findArgsFieldTypeList(orderedFieldList);
            ChatCommandContainer container = new ChatCommandContainer(
                    annotation.name(),
                    findNonOptionalArgCount(annotation.argsClass()),
                    orderedFieldList,
                    method,
                    annotation.argsClass(),
                    typeList,
                    this
            );
            UntitledBot.CommandMapv.put(annotation.name(), container);
        });
    }

    private Field[] findOrderedFieldList(Class<?> clazz){
        var fieldList = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        fieldList.removeIf(field -> !field.isAnnotationPresent(ArgField.class));
        Field[] orderedList = new Field[fieldList.size()];
        for(Field field : fieldList){ orderedList[field.getAnnotation(ArgField.class).index()] = field; }
        return orderedList;
    }

    private int findNonOptionalArgCount(Class<?> clazz){
        var fieldList =  new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        fieldList.removeIf(field -> !field.isAnnotationPresent(ArgField.class) || (field.isAnnotationPresent(ArgField.class) && field.getAnnotation(ArgField.class).optional()));
        return fieldList.size();
    }

    private Class<?>[] findArgsFieldTypeList(Field[] orderedFieldList){
        var filedTypeArray = new Class<?>[orderedFieldList.length+1];
        filedTypeArray[0] = this.getClass();
        for(int i =1; i <= orderedFieldList.length; i++){
            filedTypeArray[i] = orderedFieldList[i-1].getType();
        }
        return filedTypeArray;
    }
}
