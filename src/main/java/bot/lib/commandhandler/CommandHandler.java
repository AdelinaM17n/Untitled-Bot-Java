package bot.lib.commandhandler;

import bot.UntitledBot;
import bot.lib.commandhandler.annotation.ArgParseType;
import bot.lib.commandhandler.annotation.ArgField;
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

        ChatCommandContainer commandObject = UntitledBot.CommandMap.get(command);
        var args = parseArgument(commandArgContents,commandObject, event.getJDA());
        if (args == null && commandObject.hasNonOptionalArgs()) {
            // TODO Perhaps it should indicate the user that the args weren't correct by replying to the command here
            // Since I am trying to make this platform independent as possible I won't do it currently.
            return;
        }

        try{
            var innerClass = commandObject.argsClass();
            var constructor = innerClass.getConstructor(commandObject.getClass()); //innerClass.getDeclaredConstructor(commandObject.getArgsFieldTypeList());
            var argsInstance = constructor.newInstance(commandObject);

            var fieldListIterator = Arrays.stream(commandObject.getOrderedFieldList()).iterator();
            var argsIterator = Arrays.stream(args).iterator();
            while (fieldListIterator.hasNext() && argsIterator.hasNext()){fieldListIterator.next().set(argsInstance,argsIterator.next());}

            commandObject.executionMethod().invoke(commandObject.extensionInstance(),argsInstance,event.getMessage(),event.getGuild());
        }catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }

    }

    public Object[] parseArgument(ArrayList<String> contents, ChatCommandContainer containerObject, JDA apiWrapper){
        if(contents.size() < containerObject.nonOptionalArgCount()) return null;

        Field[] orderedList = containerObject.getOrderedFieldList();
        Object[] listObjects = new Object[orderedList.length+1];
        listObjects[0] = containerObject.extensionInstance();//commandObject.getClass().cast(commandObject);
        //HashMap<String,Object> hashMap = new HashMap<>();
        boolean hasMetCoalesc = false;
        for(int i =1; i <= orderedList.length; i++){
            if(orderedList[i-1] == null) return null;
            if(hasMetCoalesc){
                listObjects[i] = null;
            }else if(orderedList[i-1].getAnnotation(ArgField.class).type() == ArgParseType.STRING_COALESCING){
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
                    }else{
                        listObjects[i] = apiWrapper.retrieveUserById(contents.get(0)).complete();
                    }
                    orderedList[i-1] = null;
                }else {
                    listObjects[i] = null;
                }
                contents.remove(0);
                orderedList[i-1] = null;
            }
        }

        return Arrays.stream(orderedList).anyMatch(field -> field != null && !field.getAnnotation(ArgField.class).optional()) ? null : listObjects;
    }
}
