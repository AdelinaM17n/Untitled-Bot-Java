package bot.components;

import bot.UntitledBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if(event.getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith(UntitledBot.prefix))
            return;

        String command = event.getMessage().getContentRaw().substring(1).replaceAll("\\s.*", "").trim();

        if(UntitledBot.CommandMap.containsKey(command)){
            UntitledBot.CommandMap.get(command).run(new String[]{event.getMessage().getMentions(Message.MentionType.USER).stream().findFirst().get().getId()},event.getMessage(), event.getGuild());
        }
    }
}
