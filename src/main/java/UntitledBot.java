import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

import javax.security.auth.login.LoginException;

public class UntitledBot {
    public static void main(String[] args) throws LoginException {
        JDABuilder jda = JDABuilder.createDefault(args[0]);
        jda.setStatus(OnlineStatus.DO_NOT_DISTURB);
        jda.build();
    }
}
