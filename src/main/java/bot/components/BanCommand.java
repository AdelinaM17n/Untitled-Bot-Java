package bot.components;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class BanCommand extends ChatCommand{

    public BanCommand(String name) {
        super(name);
    }

    @Override
    public void run(String[] args, Message message, Guild guild) {
        guild.ban(args[0],0).complete();
        message.reply("bruh").complete();
    }
}
