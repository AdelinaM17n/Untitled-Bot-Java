package bot.components;

import bot.lib.commandhandler.ChatCommand;
import bot.lib.commandhandler.annotation.ArgInnerClass;
import bot.lib.commandhandler.annotation.ArgParseType;
import bot.lib.commandhandler.annotation.ArgField;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class BanCommand extends ChatCommand {
    private static final String COMMAND = "ban";

    public BanCommand() {
        super(COMMAND);
    }

    //@Override
    public void run(BanCommandArgs args, Message message, Guild guild) {
        guild.ban(args.targetArg.getId(),0).complete();
        message.reply(" I'm not so open-minded that I've lost my brains").complete();
        System.out.println(args.targetArg.getName());
    }

    @ArgInnerClass
    @SuppressWarnings("all")
    public class BanCommandArgs {
        @ArgField(type = ArgParseType.NORMAL, index = 0)
        private User targetArg;
        @ArgField(type = ArgParseType.STRING_COALESCING, index = 1, optional = true)
        private String reasonArg;

        public BanCommandArgs(User targetArg, String reasonArg){
            this.targetArg = targetArg;
            this.reasonArg = reasonArg;
        }
    }
}
