package bot.components;

import bot.UntitledBot;
import bot.components.annotations.ArgType;
import bot.components.annotations.CommandArg;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if(event.getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith(UntitledBot.prefix))
            return;

        String[] messageContents = event.getMessage().getContentRaw().substring(1).split("\\s");
        String command = messageContents[0];
        var commandArgContents = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(messageContents, 1, messageContents.length)));

        if(!UntitledBot.CommandMap.containsKey(command)) return;

        ChatCommand commandObject = UntitledBot.CommandMap.get(command);
        var args = parseArgument(commandArgContents,commandObject);
        if (args == null) return;

        commandObject.run(args, event.getMessage(), event.getGuild());
    }

    public HashMap<String,String> parseArgument(ArrayList<String> contents, ChatCommand commandObject){
        var fieldList = new ArrayList<>(Arrays.asList(commandObject.getClass().getDeclaredFields()));
        fieldList.removeIf(field -> !field.isAnnotationPresent(CommandArg.class));
        if(contents.size() < fieldList.size()) return null;

        Field[] orderedList = new Field[fieldList.size()];
        for(Field field : fieldList){ orderedList[field.getAnnotation(CommandArg.class).index()] = field; }

        HashMap<String,String> hashMap = new HashMap<>();
        for(Field field : orderedList){
            if(field.getAnnotation(CommandArg.class).type() == ArgType.STRING_COALESCING){
                hashMap.put(field.getName(), String.join(" ", contents));
                break;
            }else {
                hashMap.put(field.getName(), contents.get(0));
                contents.remove(0);
            }
        }
        return hashMap;
    }
}
