package bot.lib.commandhandler;

import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ChatCommand {
    private final String name;
    private final int nonOptionalArgCount;

    public ChatCommand(String name){
        this.name = name;
        nonOptionalArgCount = setNonOptionalArgCount();
    }

    public abstract void run(HashMap<String,String> args, Message message, Guild guild);

    private int setNonOptionalArgCount(){
        var fieldList =  new ArrayList<>(Arrays.stream(this.getClass().getDeclaredFields()).toList());
        fieldList.removeIf(field -> field.isAnnotationPresent(CommandArg.class) || field.getAnnotation(CommandArg.class).optional());
        return fieldList.size();
    }

    public int getNonOptionalArgCount(){
        return nonOptionalArgCount;
    }

    public boolean hasNonOptionalArgs(){
        return nonOptionalArgCount > 0;
    }

    public String getName(){
        return name;
    }
}
