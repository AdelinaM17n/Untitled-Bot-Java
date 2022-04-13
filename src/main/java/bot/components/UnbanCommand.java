package bot.components;

import bot.lib.commandhandler.ChatCommand;
import bot.lib.commandhandler.annotation.ArgType;
import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class UnbanCommand extends ChatCommand {
    private static final String COMMAND = "unban";

    @CommandArg(type = ArgType.NORMAL, index = 0)
    private User targetArg;
    @CommandArg(type = ArgType.STRING_COALESCING, index = 1, optional = true)
    private String reasonArg;

    public UnbanCommand(){
        super(COMMAND);
    }

    @Override
    public void run(HashMap<String, String> args, Message message, Guild guild){
        guild.unban(args.get("targetArg")).complete();
        message.reply("Victrix causa deis placuit sed victa Catoni.").complete();
    }
}
