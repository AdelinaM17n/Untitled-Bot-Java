package bot;

import bot.components.BanCommand;
import bot.lib.commandhandler.ChatCommandContainer;
import bot.lib.commandhandler.CommandHandler;
import bot.lib.commandhandler.Extension;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class UntitledBot {
    public static HashMap<String, ChatCommandContainer> CommandMap = new HashMap<>();
    public static String prefix = "!";

    public static void main(String[] args) throws LoginException {
        JDABuilder jda = JDABuilder.createDefault(args[0]);
        jda.setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.enableIntents(GatewayIntent.GUILD_MESSAGES);

        jda.addEventListeners(
                new CommandHandler()
        );

        addExtensions(
                new BanCommand()
        );

        jda.build();
    }

    public static void addExtensions(Extension... extensions){
        for (Extension extension : extensions) {
            extension.init();
        }
    }

}
