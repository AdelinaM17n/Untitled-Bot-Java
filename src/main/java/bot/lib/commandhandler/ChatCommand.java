package bot.lib.commandhandler;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;

public abstract class ChatCommand {
    private final String name;

    public ChatCommand(String name){
        this.name = name;
    }

    public abstract void run(HashMap<String,String> args, Message message, Guild guild);
    public String getName(){
        return name;
    }
}
