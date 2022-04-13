package bot.components;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public abstract class ChatCommand {
    private final String name;

    public ChatCommand(String name){
        this.name = name;
    }

    public abstract void run(String[] args, Message message, Guild guild);

    public void setName(String name){
        name = name;
    }
    public String getName(){
        return name;
    }
}
