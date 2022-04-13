package bot;

import bot.components.BanCommand;
import bot.components.UnbanCommand;
import bot.lib.commandhandler.ChatCommand;
import bot.lib.commandhandler.CommandHandler;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class UntitledBot {
    public static HashMap<String, ChatCommand> CommandMap = new HashMap<>();
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException {
        JDABuilder jda = JDABuilder.createDefault(args[0]);
        jda.setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.enableIntents(GatewayIntent.GUILD_MESSAGES);

        jda.addEventListeners(
                new CommandHandler()
        );

        addCommands(
                new BanCommand(),
                new UnbanCommand()
        );

        jda.build();
    }

    public static void addCommands(ChatCommand... commands){
        for (ChatCommand command : commands) {
            CommandMap.put(command.getName(), command);
        }
    }

}
