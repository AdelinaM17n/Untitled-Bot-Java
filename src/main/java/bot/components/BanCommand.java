package bot.components;

import bot.lib.commandhandler.annotation.ArgType;
import bot.lib.commandhandler.ChatCommand;
import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class BanCommand extends ChatCommand {
    private static final String COMMAND = "ban";

    @CommandArg(type = ArgType.NORMAL, index = 0)
    private User targetArg;
    @CommandArg(type = ArgType.STRING_COALESCING, index = 1, optional = true)
    private String reasonArg;

    public BanCommand() {
        super(COMMAND);
    }

    @Override
    public void run(HashMap<String,String> args, Message message, Guild guild) {
        guild.ban(args.get("targetArg"),0,args.get("reasonArg")).complete();
        message.reply(" I'm not so open-minded that I've lost my brains").complete();
    }
}
