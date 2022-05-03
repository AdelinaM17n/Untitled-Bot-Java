package bot.components;

import bot.lib.commandhandler.Extension;
import bot.lib.commandhandler.annotation.ArgField;
import bot.lib.commandhandler.annotation.ArgInnerClass;
import bot.lib.commandhandler.annotation.ArgParseType;
import bot.lib.commandhandler.annotation.ChatCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class BanCommand extends Extension {
    @ChatCommand(
            name = "ban",
            argsClass = BanCommandArgs.class,
            requiredPerms = {Permission.BAN_MEMBERS}
    )
    public void run(BanCommandArgs args, Message message, Guild guild) {
        guild.ban(args.targetArg.getId(),0).complete();
        message.reply(" I'm not so open-minded that I've lost my brains").complete();
        System.out.println(args.targetArg.getName());
    }

    @ChatCommand(
            name = "unban",
            argsClass = BanCommandArgs.class
    )
    public void unban_run(BanCommandArgs args, Message message, Guild guild) {
        guild.unban(args.targetArg.getId()).complete();
        message.reply("nihil novi sub sole").complete();
        System.out.println(args.targetArg.getName());
    }

    @ArgInnerClass
    public class BanCommandArgs {
        @ArgField(type = ArgParseType.NORMAL, index = 0)
        public User targetArg;
        @ArgField(type = ArgParseType.STRING_COALESCING, index = 1, optional = true)
        public String reasonArg;
    }
}
