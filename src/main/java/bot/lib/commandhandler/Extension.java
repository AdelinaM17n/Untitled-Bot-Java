package bot.lib.commandhandler;

import bot.UntitledBot;
import bot.lib.commandhandler.annotation.ArgField;
import bot.lib.commandhandler.annotation.ChatCommand;
import bot.lib.commandhandler.annotation.NoArgs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Extension {
    public void init(){
        var methods = Arrays.stream(this.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ChatCommand.class));
        methods.forEach(method -> {
            var annotation = method.getAnnotation(ChatCommand.class);

            if(UntitledBot.CommandMap.containsKey(annotation.name())) {
                System.err.println("Two commands cannot have the same name");
                return;
            }

            var orderedFieldList = findOrderedFieldList(annotation.argsClass());

            ChatCommandContainer container = new ChatCommandContainer(
                    findNonOptionalArgCount(annotation.argsClass()),
                    orderedFieldList,
                    method,
                    annotation.argsClass(),
                    //typeList,
                    annotation.requiredPerms(),
                    this
            );
            UntitledBot.CommandMap.put(annotation.name(), container);
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
        if(clazz == NoArgs.class) return 0;
        var fieldList =  new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        fieldList.removeIf(field -> !field.isAnnotationPresent(ArgField.class) || (field.isAnnotationPresent(ArgField.class) && field.getAnnotation(ArgField.class).optional()));
        return fieldList.size();
    }

}
