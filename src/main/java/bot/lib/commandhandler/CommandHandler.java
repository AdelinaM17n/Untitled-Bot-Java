package bot.lib.commandhandler;

import bot.UntitledBot;
import bot.lib.commandhandler.annotation.ArgType;
import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
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

        System.out.println(commandArgContents.get(0));

        ChatCommand commandObject = UntitledBot.CommandMap.get(command);
        var args = parseArgument(commandArgContents,commandObject, event.getJDA());
        if (args == null && commandObject.hasNonOptionalArgs()) {
            // TODO Perhaps it should indicate the user that the args weren't correct by replying to the command here
            // Since I am trying to make this platform independent as possible I won't do it currently.
            return;
        }

        commandObject.run(args, event.getMessage(), event.getGuild());
    }

    public HashMap<String,Object> parseArgument(ArrayList<String> contents, ChatCommand commandObject, JDA apiWrapper){
        if(contents.size() < commandObject.getNonOptionalArgCount()) return null;

        Field[] orderedList = commandObject.getOrderedFieldList();
        HashMap<String,Object> hashMap = new HashMap<>();

        for(int i =0; i < orderedList.length; i++){
            if(orderedList[i].getAnnotation(CommandArg.class).type() == ArgType.STRING_COALESCING){
                hashMap.put(orderedList[i].getName(), String.join(" ", contents));
                orderedList[i] = null;
                break;
            }else {
                var orderedListType = orderedList[i].getType();
                if(orderedListType == String.class){
                    hashMap.put(orderedList[i].getName(), contents.get(0));
                }else if (orderedListType == User.class){
                    if(contents.get(0).startsWith("<@"))
                        hashMap.put(orderedList[i].getName(),apiWrapper.retrieveUserById(contents.get(0).substring(3,21)));
                    else
                        hashMap.put(orderedList[i].getName(),apiWrapper.retrieveUserById(contents.get(0)));
                }
                contents.remove(0);
                orderedList[i] = null;
            }
        }

        return Arrays.stream(orderedList).anyMatch(field -> field != null && !field.getAnnotation(CommandArg.class).optional()) ? null : hashMap;
    }
}
