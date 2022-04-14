package bot.lib.commandhandler;

import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ChatCommand {
    private final String name;
    private final int nonOptionalArgCount;
    private final Field[] orderedFieldList;

    public ChatCommand(String name){
        this.name = name;
        nonOptionalArgCount = setNonOptionalArgCount();
        orderedFieldList = setOrderedFieldList();
    }

    public abstract void run(HashMap<String,String> args, Message message, Guild guild);

    private int setNonOptionalArgCount(){
        var fieldList =  new ArrayList<>(Arrays.stream(this.getClass().getDeclaredFields()).toList());
        fieldList.removeIf(field -> !field.isAnnotationPresent(CommandArg.class) || (field.isAnnotationPresent(CommandArg.class) && field.getAnnotation(CommandArg.class).optional()));
        return fieldList.size();
    }
    private Field[] setOrderedFieldList(){
        var fieldList = new ArrayList<>(Arrays.asList(this.getClass().getDeclaredFields()));
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

    public String getName(){
        return name;
    }
}
