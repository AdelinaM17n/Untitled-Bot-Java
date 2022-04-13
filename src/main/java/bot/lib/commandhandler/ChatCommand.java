package bot.lib.commandhandler;

import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class ChatCommand {
    private final String name;

    public ChatCommand(String name){
        this.name = name;
    }

    public abstract void run(HashMap<String,String> args, Message message, Guild guild);

    public boolean hasNonOptionalArgs(){
        var fieldList =  new ArrayList<>(Arrays.stream(this.getClass().getDeclaredFields()).toList());
        return fieldList.stream().anyMatch(field -> field.isAnnotationPresent(CommandArg.class) && !field.getAnnotation(CommandArg.class).optional());
    }

    public String getName(){
        return name;
    }
}
