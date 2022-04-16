package bot.lib.commandhandler;

import bot.UntitledBot;
import bot.lib.commandhandler.annotation.ArgType;
import bot.lib.commandhandler.annotation.CommandArg;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

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

        try{
            var innerClass = commandObject.getArgsClass();
            var constructor = innerClass.getDeclaredConstructor(commandObject.getArgsFieldTypeList());

            var argsInstance = innerClass.cast(constructor.newInstance(args));
            commandObject.getExecutionMethod().invoke(commandObject,argsInstance,event.getMessage(),event.getGuild());

        }catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }

        //commandObject.run(args, event.getMessage(), event.getGuild());
    }

    public Object[] parseArgument(ArrayList<String> contents, ChatCommand commandObject, JDA apiWrapper){
        if(contents.size() < commandObject.getNonOptionalArgCount()) return null;

        Field[] orderedList = commandObject.getOrderedFieldList();
        Object[] listObjects = new Object[orderedList.length+1];
        listObjects[0] = commandObject.getClass().cast(commandObject);
        //HashMap<String,Object> hashMap = new HashMap<>();
        boolean hasMetCoalesc = false;
        for(int i =1; i <= orderedList.length; i++){
            if(hasMetCoalesc){
                listObjects[i] = null;
            }else if(orderedList[i-1].getAnnotation(CommandArg.class).type() == ArgType.STRING_COALESCING){
                listObjects[i] =  String.join(" ", contents);
                orderedList[i-1] = null;
                hasMetCoalesc = true;
            }else {
                var orderedListType = orderedList[i-1].getType();
                if(orderedListType == String.class){
                    listObjects[i] = contents.get(0);
                    orderedList[i-1] = null;
                }else if (orderedListType == User.class){
                    if(contents.get(0).startsWith("<@")){
                        listObjects[i] = apiWrapper.retrieveUserById(contents.get(0).substring(2, 20)).complete();
                        orderedList[i-1] = null;
                    }else{
                        listObjects[i] = apiWrapper.retrieveUserById(contents.get(0)).complete();
                        orderedList[i-1] = null;
                    }
                }else {
                    listObjects[i] = null;
                }
                contents.remove(0);
                orderedList[i-1] = null;
            }
        }

        return Arrays.stream(orderedList).anyMatch(field -> field != null && !field.getAnnotation(CommandArg.class).optional()) ? null : listObjects;
    }
}
