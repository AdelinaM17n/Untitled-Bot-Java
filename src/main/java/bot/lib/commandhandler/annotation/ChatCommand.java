package bot.lib.commandhandler.annotation;

public @interface ChatCommand {
    String name();
    Class<?> argsClass();
}
